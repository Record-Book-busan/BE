package busim.kkilogbu.global.redis;

import static org.springframework.data.geo.Metrics.*;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import busim.kkilogbu.api.restaurantAPI.domain.dto.RestaurantMapper;
import busim.kkilogbu.api.restaurantAPI.domain.dto.RestaurantResponseDto;
import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;
import busim.kkilogbu.api.restaurantAPI.repository.RestaurantRepository;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristMapper;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristResponseDto;
import busim.kkilogbu.api.touristAPI.repository.TouristRepository;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import busim.kkilogbu.global.ZoomLevel;
import busim.kkilogbu.global.redis.dto.Cluster;
import busim.kkilogbu.place.dto.PlaceDetailResponse;
import busim.kkilogbu.place.dto.PlaceMarkResponse;
import busim.kkilogbu.place.repository.PlaceRepository;
import busim.kkilogbu.record.dto.RecordDetailResponse;
import busim.kkilogbu.record.dto.RecordMarkResponse;
import busim.kkilogbu.record.repository.RecordRepository;
import ch.hsr.geohash.GeoHash;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;




import static org.springframework.data.geo.Metrics.KILOMETERS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;
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
import busim.kkilogbu.global.ZoomLevel;
import busim.kkilogbu.place.dto.PlaceMarkResponse;
import busim.kkilogbu.place.repository.PlaceRepository;
import busim.kkilogbu.record.dto.RecordMarkResponse;
import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.place.dto.RestaurantCategory;
import busim.kkilogbu.place.dto.TouristCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RedisService {
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;
	private final RecordRepository recordRepository;
	private final PlaceRepository placeRepository;
	private final ToiletDataRepository toiletDataRepository;
	private final ParkingRepository parkingRepository;
	private final RestaurantRepository restaurantRepository;
	private final TouristRepository touristRepository;

	/**
	 * 맛집 카테고리에 따른 Redis 조회
	 */
	public List<PlaceMarkResponse> getRestaurantPlacesInRedis(double lat, double lng, ZoomLevel level, List<RestaurantCategory> restaurantCategories) {
		List<PlaceMarkResponse> places = new ArrayList<>();
		for (RestaurantCategory category : restaurantCategories) {
			String key = "geo:place:" + category.name();  // 카테고리별로 키를 생성
			places.addAll(getPlacesFromRedis(lat, lng, level, key, PlaceMarkResponse.class));
		}
		return places;
	}

	/**
	 * 관광 카테고리에 따른 Redis 조회
	 */
	public List<PlaceMarkResponse> getTouristPlacesInRedis(double lat, double lng, ZoomLevel level, List<TouristCategory> touristCategories) {
		List<PlaceMarkResponse> places = new ArrayList<>();
		for (TouristCategory category : touristCategories) {
			String key = "geo:place:" + category.name();  // 카테고리별로 키를 생성
			places.addAll(getPlacesFromRedis(lat, lng, level, key, PlaceMarkResponse.class));
		}
		return places;
	}

	/**
	 * 레디스에서 장소 정보를 가져오는 공통 메서드
	 */
	private <T> List<T> getPlacesFromRedis(double lat, double lng, ZoomLevel level, String key, Class<T> type) {
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		Point point = new Point(lng, lat);
		Circle circle = new Circle(point, new Distance(level.getKilometer(), KILOMETERS));

		List<GeoResult<RedisGeoCommands.GeoLocation<String>>> geoResults = geoOperations.radius(key, circle).getContent();

		return geoResults.stream()
				.map(GeoResult::getContent)
				.map(RedisGeoCommands.GeoLocation::getName)
				.map(json -> {
					try {
						return objectMapper.readValue(json, type);
					} catch (IOException e) {
						log.error("Error reading from Redis", e);
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * 맛집 및 관광 카테고리를 함께 조회하는 경우
	 */
	public List<PlaceMarkResponse> getPlacesInToRedis(double lat, double lng, ZoomLevel level, List<RestaurantCategory> restaurantCategories, List<TouristCategory> touristCategories) {
		List<PlaceMarkResponse> places = new ArrayList<>();

		// 맛집 카테고리 조회
		if (restaurantCategories != null && !restaurantCategories.isEmpty()) {
			places.addAll(getRestaurantPlacesInRedis(lat, lng, level, restaurantCategories));
		}

		// 관광 카테고리 조회
		if (touristCategories != null && !touristCategories.isEmpty()) {
			places.addAll(getTouristPlacesInRedis(lat, lng, level, touristCategories));
		}

		return places;
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
			saveTotalPlaceInRedis(toilet.getLatitude(), toilet.getLongitude(), input, "toilet", null);
		});
	}

	/**
	 * 주차장 데이터를 Redis에 저장
	 */
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
			saveTotalPlaceInRedis(parking.getXCdnt(), parking.getYCdnt(), input, "park", null);
		});
	}

	/**
	 * Record 데이터를 Redis에 저장
	 */
	public void saveRecordDataInRedis(){
		List<RecordMarkResponse> all = recordRepository.findAll().stream()
				.map(record -> RecordMarkResponse.builder()
						.id(record.getId())
						.lat(record.getAddressInfo().getLatitude())
						.lng(record.getAddressInfo().getLongitude())
						.cat1(record.getCat1())
						.cat2(record.getCat2())
						.imageUrl(record.getContents().getImageUrl())
						.build())
				.toList();
		all.forEach(record -> saveTotalPlaceInRedis(record.getLat(), record.getLng(), record, "record", record.getCat2()));
	}


	/**
	 * Restaurant 데이터를 Redis에 저장
	 */
	public void saveRestaurantDataInRedis(){
		List<RestaurantResponseDto> allRestaurants = restaurantRepository.findAll().stream()
				.map(RestaurantMapper::toRestaurantResponseDto)  // Mapper 클래스를 사용하여 변환
				.toList();

		// 각 RestaurantResponseDto 데이터를 Redis에 저장
		allRestaurants.forEach(restaurant -> saveTotalPlaceInRedis(
				restaurant.getLatitude(),
				restaurant.getLongitude(),
				restaurant,
				"restaurant",
				restaurant.getType()));  // type을 카테고리처럼 사용
	}


	/**
	 * Tourist 데이터를 Redis에 저장
	 */
	public void saveTouristDataInRedis() {
		// Tourist 엔티티를 TouristResponseDto로 변환하여 리스트로 만듭니다.
		List<TouristResponseDto> allTourists = touristRepository.findAll().stream()
				.map(TouristMapper::toTouristResponseDto)  // Mapper 클래스를 사용하여 변환
				.toList();

		// 각 TouristResponseDto 데이터를 Redis에 저장
		allTourists.forEach(tourist -> saveTotalTouristInRedis(
				tourist.getLatitude(),
				tourist.getLongitude(),
				tourist,
				"tourist",
				tourist.getCategoryLarge()));  // categoryLarge를 사용해 대분류 저장
	}

	/**
	 * redis에 위치 정보 저장
	 */
	public void saveTotalPlaceInRedis(double lat, double lng, Object object, String type, Long category) {
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "geo:" + type + (category != null ? ":" + category : "");

		Point point = new Point(lng, lat);
		try {
			String input = objectMapper.writeValueAsString(object);
			Long added = geoOperations.add(key, point, input);
			log.info("Added {} elements to {}", added, key);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Redis에 Tourist 데이터를 저장하는 메서드
	 */
	public void saveTotalTouristInRedis(Double lat, Double lng, TouristResponseDto touristDto, String type, String categoryLarge) {
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "geo:" + type;

		// 대분류 카테고리를 Redis 키에 포함
		if (categoryLarge != null && !categoryLarge.isEmpty()) {
			key += ":" + categoryLarge;
		}

		Point point = new Point(lng, lat);  // 위도와 경도를 Point로 변환
		try {
			// TouristResponseDto 객체를 JSON 형태로 변환하여 저장
			String input = objectMapper.writeValueAsString(touristDto);
			geoOperations.add(key, point, input);  // Redis에 저장
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Tourist 데이터 Redis 저장 중 오류 발생", e);
		}
	}


	/**
	 * Redis 데이터 주기적 업데이트 (스케줄링)
	 */
	@Scheduled(cron = "0 0 0 19 * *", zone = "Asia/Seoul")
	public void dailyRedisUpdateAt4AM() {
		deleteKey();
		saveRecordDataInRedis();
		saveTouristDataInRedis();
		saveRestaurantDataInRedis();
		saveToiletDataInRedis();
		saveParkDataInRedis();
	}

	private void deleteKey() {
		for (int i = 0; i < 256; i++) {
			redisTemplate.delete("geo:record:" + i);
		}
		redisTemplate.delete("geo:place");
		redisTemplate.delete("geo:toilet");
		redisTemplate.delete("geo:park");
	}

	/*--------------- front 와 논의후 사용결정 ------------------*/
	/**
	 * redis에 위치 정보 조회
	 */
	public List<Cluster> getPlacesInRedis(double lat, double lng, ZoomLevel level, String type, Long category){
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "geo:" + type + ":" + category;
		Point point = new Point(lng, lat);
		Circle circle = new Circle(point, new Distance(level.getKilometer(), KILOMETERS));

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
	public List<ToiletDataResponse> getToiletList(double lat, double lng, double radius){
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "geo:toilet";
		Point point = new Point(lng, lat);
		Circle circle = new Circle(point, new Distance(radius, KILOMETERS));

		List<GeoResult<RedisGeoCommands.GeoLocation<String>>> geoResults = geoOperations.radius(key, circle).getContent();

		// 조회된 결과를 ToiletDataResponse 객체로 변환
		return geoResults.stream()
			.map(GeoResult::getContent)
			.map(RedisGeoCommands.GeoLocation::getName)
			.map(json -> {
				try {
					return objectMapper.readValue(json, ToiletDataResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			})
			.filter(Objects::nonNull) // null 필터링
			.collect(Collectors.toList());
	}
}
