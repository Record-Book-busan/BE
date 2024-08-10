package busim.kkilogbu.global.redis;

import static org.springframework.data.geo.Metrics.*;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import busim.kkilogbu.api.ParkingAPI.domain.dto.ParkingDataResponse;
import busim.kkilogbu.api.ParkingAPI.domain.entity.ParkingData;
import busim.kkilogbu.api.ParkingAPI.repository.ParkingRepository;
import busim.kkilogbu.api.restroomAPI.domain.dto.ToiletDataResponse;
import busim.kkilogbu.api.restroomAPI.domain.entity.ToiletData;
import busim.kkilogbu.api.restroomAPI.repository.ToiletDataRepository;
import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.global.redis.dto.Cluster;
import busim.kkilogbu.place.dto.PlaceDetailResponse;
import busim.kkilogbu.record.dto.RecordDetailResponse;
import ch.hsr.geohash.GeoHash;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;
	private final ToiletDataRepository toiletDataRepository;
	private final ParkingRepository parkingRepository;
	/**
	 * redis에 위치 정보 저장
	 */
	public void savePlacesInRedis(double lat, double lng, Object object, String type, Long category) {
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "";
		if(type.equals("record") || type.equals("place")) {
			key = "geo:" + type + ":" + category;
		}else if(type.equals("toilet") || type.equals("park")){
			key = "geo:" + type;
		}
		else if(!StringUtils.hasText(type)) {
			log.error("type이 없습니다.");
			throw new BaseException("type이 없습니다.", BAD_REQUEST);
		}
		Point point = new Point(lng, lat);
		try {
			String input = objectMapper.writeValueAsString(object);
			log.info("input : {}", input);
			log.info("point : {}", point.toString());
			Long added = geoOperations.add(key, point, input);
			log.info("Added {} elements to {}", added, key);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * redis에 위치 정보 조회
	 */
	public List<Cluster> getPlacesInRedis(double lat, double lng, double radius, String type, Long category){
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "geo:" + type + ":" + category;
		Point point = new Point(lng, lat);
		Circle circle = new Circle(point, new Distance(radius, KILOMETERS));

		List<GeoResult<RedisGeoCommands.GeoLocation<String>>> geoResults = geoOperations.radius(key, circle).getContent();

		// 조회된 결과를 RecordDetail 또는 PlaceDetail 객체로 변환
		List<Object> record = geoResults.stream()
			.map(GeoResult::getContent)
			.map(RedisGeoCommands.GeoLocation::getName)
			.map(json -> {
				try {
					// TODO : 지도상에서 보여줄 데이터이기 때문에 그냥 사진이랑 id값만 있어도 될거 같음
					if (type.equals("record")) {
						return objectMapper.readValue(json, RecordDetailResponse.class);
					} else {
						return objectMapper.readValue(json, PlaceDetailResponse.class);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			})
			.filter(Objects::nonNull) // null 필터링
			.collect(Collectors.toList());

		return clusterPlaces(record, 6, type);
	}
	private List<Cluster> clusterPlaces(List<Object> places, int zoomLevel, String type){
		// GeoHash precision level 설정 (zoomLevel에 따라 달라짐)
		int precision = getGeoHashPrecision(zoomLevel);

		// GeoHash를 사용하여 클러스터링
		Map<String, List<Object>> clusters = places.stream()
			.collect(Collectors.groupingBy(place -> {
				String geoHash;
				if(type == "record"){
					geoHash = GeoHash.withCharacterPrecision(((RecordDetailResponse)place).getLat(), ((RecordDetailResponse)place).getLng(), precision)
						.toBase32();
				}else{
					geoHash = GeoHash.withCharacterPrecision(((PlaceDetailResponse)place).getLat(), ((PlaceDetailResponse)place).getLng(), precision)
						.toBase32();
				}
				return geoHash;
			}));
		// Cluster 객체 생성
		return clusters.entrySet().stream()
			.map(entry -> {
				String geoHash = entry.getKey();
				List<Object> clusterRecords = entry.getValue();
				GeoHash hash = GeoHash.fromGeohashString(geoHash);
				double latitude = hash.getOriginatingPoint().getLatitude();
				double longitude = hash.getOriginatingPoint().getLongitude();
				return new Cluster(latitude, longitude, clusterRecords);
			})
			.collect(Collectors.toList());
	}

	private int getGeoHashPrecision(int zoomLevel) {
		// zoomLevel에 따라 GeoHash precision level 설정 (예: 대략적인 값)
		if (zoomLevel >= 0 && zoomLevel <= 5) {
			return 4; // 낮은 precision (큰 클러스터)
		} else if (zoomLevel >= 6 && zoomLevel <= 10) {
			return 6; // 중간 precision (중간 클러스터)
		} else {
			return 8; // 높은 precision (작은 클러스터)
		}
	}
	public <T> List<T> getPublicPlacesList(double lat, double lng, double radius, Class<T> type){
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "geo:" + (type.isInstance(ToiletDataResponse.class) ? "toilet" : "park");
		Point point = new Point(lng, lat);
		Circle circle = new Circle(point, new Distance(radius, KILOMETERS));

		List<GeoResult<RedisGeoCommands.GeoLocation<String>>> geoResults = geoOperations.radius(key, circle).getContent();

		// 조회된 결과를 ToiletDataResponse 객체로 변환
		return geoResults.stream()
			.map(GeoResult::getContent)
			.map(RedisGeoCommands.GeoLocation::getName)
			.map(json -> {
				try {
					return objectMapper.readValue(json, type);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			})
			.filter(Objects::nonNull) // null 필터링
			.collect(Collectors.toList());
	}

	/**
	 * 화장실 데이터를 Redis에 저장
	 */
	public void saveToiletDataInRedis() {
		List<ToiletData> all = toiletDataRepository.findAll();
		all.forEach(toilet -> {
			ToiletDataResponse input = ToiletDataResponse.builder()
				.latitude(toilet.getLatitude())
				.longitude(toilet.getLongitude())
				.phoneNumber(toilet.getPhoneNumber())
				.openingHours(toilet.getOpeningHours())
				.toiletName(toilet.getToiletName())
				.build();
			this.savePlacesInRedis(toilet.getLatitude(), toilet.getLongitude(), input, "toilet", null);
		});
	}

	public void saveParkDataInRedis() {
		List<ParkingData> all = parkingRepository.findAll();
		all.forEach(parking -> {
			ParkingDataResponse input = ParkingDataResponse.builder()
				.id(parking.getId())
				.lat(parking.getXCdnt())
				.lng(parking.getYCdnt())
				.jibunAddr(parking.getJibunAddr())
				.pkFm(parking.getPkFm())
				.pkCnt(parking.getPkCnt())
				.svcSrtTe(parking.getSvcSrtTe())
				.svcEndTe(parking.getSvcEndTe())
				.tenMin(parking.getTenMin())
				.ftDay(parking.getFtDay())
				.ftMon(parking.getFtMon())
				.pkGubun(parking.getPkGubun())
				.build();
			this.savePlacesInRedis(parking.getXCdnt(), parking.getYCdnt(), input, "park", null);
		});
	}
}
