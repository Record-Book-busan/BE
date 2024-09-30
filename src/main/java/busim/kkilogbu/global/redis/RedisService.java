package busim.kkilogbu.global.redis;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import busim.kkilogbu.api.restaurantAPI.domain.dto.RestaurantMapper;
import busim.kkilogbu.api.restaurantAPI.domain.dto.RestaurantResponseDto;
import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;
import busim.kkilogbu.api.restaurantAPI.repository.RestaurantRepository;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristMapper;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristResponseDto;
import busim.kkilogbu.api.touristAPI.repository.TouristRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;




import static org.springframework.data.geo.Metrics.KILOMETERS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.List;
import java.util.Objects;

import busim.kkilogbu.place.dto.RestaurantCategory;
import busim.kkilogbu.place.dto.TouristCategory;

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
	public List<PlaceMarkResponse> getRestaurantPlacesNew(double lat, double lng, ZoomLevel level, List<RestaurantCategory> restaurantCategories) {
		List<PlaceMarkResponse> places = new ArrayList<>();

		log.info("맛집 카테고리 조회 시작: lat={}, lng={}, level={}", lat, lng, level);

		for (RestaurantCategory category : restaurantCategories) {
			String key = "geo:restaurant:" + category.name();
			log.info("레디스에서 조회할 키: {}", key);

			// Redis에 저장된 데이터의 총 개수 로깅
			Long totalStored = redisTemplate.opsForZSet().zCard(key);
			log.info("Redis에 저장된 '{}' 카테고리의 총 개수: {}", category.name(), totalStored);

			List<RestaurantResponseDto> placesFromRedisNew = getPlacesFromRedisNew(lat, lng, level, key,
				RestaurantResponseDto.class);
			placesFromRedisNew.forEach(place -> {
				places.add(PlaceMarkResponse.builder()
								.id(place.getId())
								.lat(place.getLatitude())
								.lng(place.getLongitude())
								.imageUrl(place.getImageUrl())
								.category(category.name())
								.type("restaurant")  // "restaurant"으로 타입 설정
								.build());
			});
		}

		log.info("맛집 카테고리 조회 완료: 총 조회된 장소 개수 = {}", places.size());
		return places;
	}

	/**
	 * 관광 카테고리에 따른 Redis 조회
	 */
	public List<PlaceMarkResponse> getTouristPlacesNew(double lat, double lng, ZoomLevel level, List<TouristCategory> touristCategories) {
		List<PlaceMarkResponse> places = new ArrayList<>();

		log.info("관광 카테고리 조회 시작: lat={}, lng={}, level={}", lat, lng, level);

		for (TouristCategory category : touristCategories) {
			String key = "geo:tourist:" + category.name();
			log.info("레디스에서 조회할 키: {}", key);

			// Redis에 저장된 데이터의 총 개수 로깅
			Long totalStored = redisTemplate.opsForZSet().zCard(key);
			log.info("Redis에 저장된 '{}' 카테고리의 총 개수: {}", category.name(), totalStored);

			List<TouristResponseDto> placesFromRedisNew = getPlacesFromRedisNew(lat, lng, level, key,
				TouristResponseDto.class);
			placesFromRedisNew.forEach(place -> {
				places.add(PlaceMarkResponse.builder()
						.id(place.getId())
						.lat(place.getLatitude())
						.lng(place.getLongitude())
						.imageUrl(place.getImageUrl())
						.category(category.name())
						.type("tourist")  // "tourist"로 타입 설정
						.build());
			});
		}

		log.info("관광 카테고리 조회 완료: 총 조회된 장소 개수 = {}", places.size());
		return places;
	}

	/**
	 * 맛집 및 관광 카테고리를 함께 조회
	 */
	public List<PlaceMarkResponse> getAllPlacesNew(double lat, double lng, ZoomLevel level, List<RestaurantCategory> restaurantCategories, List<TouristCategory> touristCategories) {
		List<PlaceMarkResponse> places = new ArrayList<>();

		log.info("전체 카테고리 조회 시작: lat={}, lng={}, level={}", lat, lng, level);

		// 맛집 카테고리 조회
		if (restaurantCategories != null && !restaurantCategories.isEmpty()) {
			log.info("맛집 카테고리 조회 실행: 카테고리 개수 = {}", restaurantCategories.size());
			places.addAll(getRestaurantPlacesNew(lat, lng, level, restaurantCategories));
		} else {
			log.info("맛집 카테고리가 존재하지 않음");
		}

		// 관광 카테고리 조회
		if (touristCategories != null && !touristCategories.isEmpty()) {
			log.info("관광 카테고리 조회 실행: 카테고리 개수 = {}", touristCategories.size());
			places.addAll(getTouristPlacesNew(lat, lng, level, touristCategories));
		} else {
			log.info("관광 카테고리가 존재하지 않음");
		}

		log.info("전체 조회 완료: 총 조회된 장소 개수 = {}", places.size());
		return places;
	}

	/**
	 * 레디스에서 장소 정보를 가져오는 맛집, 관광 공통 메서드
	 */
	private <T> List<T> getPlacesFromRedisNew(double lat, double lng, ZoomLevel level, String key, Class<T> type) {
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

//	위에는 코드는 관광, 맛집 전용

	/**
	 * 레디스에서 장소 정보를 가져오는 공통 메서드
	 */
	public <T> List<T> getPlacesFromRedis(double lat, double lng, ZoomLevel level, String key, Class<T> type) {
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

	public <T> List<T> getPublicPlacesInRedis(double lat, double lng, ZoomLevel level, Class<T> type){
		String key = "geo:" + getRedisKeyByType(type);

		List<GeoResult<RedisGeoCommands.GeoLocation<String>>> geoResults = getGeoResultsInRedis(
				lat, lng, level, key);

		// 조회된 결과를 ToiletDataResponse 객체로 변환
		return changeGeoHashToReturnType(type, geoResults);
	}
	private <T> List<T> changeGeoHashToReturnType(Class<T> type, List<GeoResult<RedisGeoCommands.GeoLocation<String>>> geoResults) {
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



	private List<GeoResult<RedisGeoCommands.GeoLocation<String>>> getGeoResultsInRedis(double lat, double lng, ZoomLevel level,
																					   String key) {
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		Point point = new Point(lng, lat);
		Circle circle = new Circle(point, new Distance(level.getKilometer(), KILOMETERS));

		return geoOperations.radius(key, circle).getContent();
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
			saveTotalPlaceInRedis(toilet.getLatitude(), toilet.getLongitude(), input, "toilet");
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
			saveTotalPlaceInRedis(parking.getXCdnt(), parking.getYCdnt(), input, "park");
		});
	}

	/**
	 * Records 데이터를 Redis에 저장
	 */
	public void saveRecordDataInRedis(){
		List<RecordMarkResponse> all = recordRepository.findAll().stream()
				.map(record -> RecordMarkResponse.builder()
						.id(record.getId())
						.lat(record.getAddressInfo().getLatitude())
						.lng(record.getAddressInfo().getLongitude())
						.imageUrl(record.getContents().getImageUrl())
						.build())
				.toList();
		all.forEach(record -> saveTotalPlaceInRedis(record.getLat(), record.getLng(), record, "record"));
	}


	/**
	 * Restaurant 데이터를 Redis에 저장
	 */
	public void saveRestaurantDataInRedis(){
		int batchSize = 100; // 한 번에 처리할 배치 크기
		int page = 0;
		boolean hasMoreData = true;

		while (hasMoreData) {
			PageRequest pageRequest = PageRequest.of(page, batchSize);
			Page<Restaurant> restaurantPage = restaurantRepository.findAll(pageRequest);

			List<RestaurantResponseDto> allRestaurants = restaurantPage.stream()
				.map(RestaurantMapper::toRestaurantResponseDto)  // Mapper 클래스를 사용하여 변환
				.toList();

			// 각 RestaurantResponseDto 데이터를 Redis에 저장
			allRestaurants.forEach(restaurant -> saveTotalRestaurantInRedis(
				restaurant.getLatitude(),
				restaurant.getLongitude(),
				restaurant,
				"restaurant",
				RestaurantCategory.fromType(restaurant.getType())));  // type을 카테고리처럼 사용

			// 다음 페이지로 이동
			page++;
			// 더 이상 데이터가 없을 경우 루프를 종료
			hasMoreData = restaurantPage.hasNext();
		}
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
				TouristCategory.fromString(tourist.getCategoryLarge())));  // categoryLarge를 사용해 대분류 저장
	}

	/**
	 * Redis 데이터 주기적 업데이트 (스케줄링)
	 */
	@Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
	public void dailyRedisUpdateAt4AM() {
		deleteKey();
		saveTouristDataInRedis();
		saveParkDataInRedis();
		saveRecordDataInRedis();
		saveToiletDataInRedis();
	}

	@Scheduled(cron = "0 30 4 * * *", zone = "Asia/Seoul")
	public void dailyRestaurantRedisUpdateAt4AM() {
		saveRestaurantDataInRedis();
	}

	private void deleteKey() {
		redisTemplate.delete("geo:record");
		// 단일 키 삭제
		redisTemplate.delete("geo:toilet");
		redisTemplate.delete("geo:park");

		// 추가로 tourist와 restaurant 데이터를 삭제
		redisTemplate.delete("geo:tourist");
		redisTemplate.delete("geo:restaurant");

		//  각 tourist 및 restaurant의 세부 카테고리별로 데이터를 저장했다면 해당 키들도 삭제
		deleteKeysWithPattern("geo:restaurant:*");
		deleteKeysWithPattern("geo:tourist:*");
	}

	/**
	 * 주어진 패턴에 맞는 Redis 키들을 삭제하는 메서드
	 */
	private void deleteKeysWithPattern(String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);  // 패턴에 맞는 모든 키를 가져옴
		if (keys != null && !keys.isEmpty()) {
			redisTemplate.delete(keys);  // 해당 키들을 삭제
		}
	}


	public String getRedisKeyByType(Class type){
		if(type == RecordMarkResponse.class) {
			return "record";
		}else if(type == PlaceMarkResponse.class) {
			return "place";
		}else if(type == ToiletDataResponse.class) {
			return "toilet";
		}else if(type == ParkingDataResponse.class) {
			return "park";
		}
		throw new BaseException("type이 없습니다.", BAD_REQUEST);
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

	/**
	 * redis에 위치 정보 저장
	 */
	public void saveTotalPlaceInRedis(double lat, double lng, Object object, String type) {
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "geo:" + type;

		Point point = new Point(lng, lat);
		try {
			String input = objectMapper.writeValueAsString(object);
			Long added = geoOperations.add(key, point, input);
			log.info("Added {} elements to {}", added, key);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}


	/*
	 * Redis에 Restaurant 데이터를 저장하는 메서드
	 */
	public void saveTotalRestaurantInRedis(Double lat, Double lng, RestaurantResponseDto restaurantDto, String type, RestaurantCategory category) {
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "geo:" + type;

		// RestaurantCategory enum을 Redis 키에 포함
		if (category != null) {
			key += ":" + category.name();  // enum의 이름을 사용하여 Redis 키에 포함
		}

		Point point = new Point(lng, lat);  // 위도와 경도를 Point로 변환
		try {
			// RestaurantResponseDto 객체를 JSON 형태로 변환하여 Redis에 저장
			String input = objectMapper.writeValueAsString(restaurantDto);
			geoOperations.add(key, point, input);  // Redis에 저장
			log.info("Restaurant 데이터 저장 완료: key={}, lat={}, lng={}", key, lat, lng);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Restaurant 데이터 Redis 저장 중 오류 발생", e);
		}
	}

	/**
	 * Redis에 Tourist 데이터를 저장하는 메서드
	 */
	public void saveTotalTouristInRedis(Double lat, Double lng, TouristResponseDto touristDto, String type, TouristCategory categoryLarge) {
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "geo:" + type;

		// TouristCategory enum에서 이름을 가져와 Redis 키에 포함
		if (categoryLarge != null) {
			key += ":" + categoryLarge.name();  // TouristCategory enum의 이름을 키에 포함
		}

		Point point = new Point(lng, lat);  // 위도와 경도를 Point로 변환
		try {
			// TouristResponseDto 객체를 JSON 형태로 변환하여 저장
			String input = objectMapper.writeValueAsString(touristDto);
			geoOperations.add(key, point, input);  // Redis에 저장
			log.info("Tourist 데이터 저장 완료: key={}, lat={}, lng={}", key, lat, lng);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Tourist 데이터 Redis 저장 중 오류 발생", e);
		}
	}



}
