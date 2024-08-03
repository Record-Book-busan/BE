package busim.kkilogbu.global.redis;

import static org.springframework.data.geo.Metrics.*;

import java.io.IOException;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import busim.kkilogbu.place.dto.PlaceDetailResponse;
import busim.kkilogbu.record.dto.RecordDetailResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;
	/**
	 * redis에 위치 정보 저장
	 */
	public void savePlacesInRedis(double lat, double lng, Object object, String type, Long category) {
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		String key = "geo:" + type + ":" + category;
		Point point = new Point(lat, lng);
		try {
			geoOperations.add(key, point, objectMapper.writeValueAsString(object));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * redis에 위치 정보 조회
	 */
	public List<Object> getPlacesInRedis(double lat, double lng, double radius, String type, Long category){
		GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
		// TODO : recrod와 place를 구분해서 저장할까?
		String key = "geo:" + type + ":" + category;
		Point point = new Point(lat, lng);
		Circle circle = new Circle(point, new Distance(radius, KILOMETERS));

		List<GeoResult<RedisGeoCommands.GeoLocation<String>>> geoResults = geoOperations.radius(key, circle).getContent();

		// 조회된 결과를 RecordDetail 또는 PlaceDetail 객체로 변환
		return geoResults.stream()
			.map(GeoResult::getContent)
			.map(RedisGeoCommands.GeoLocation::getName)
			.map(json -> {
				try {
					if(type.equals("record")) {
						return objectMapper.readValue(json, RecordDetailResponse.class);
					}
					else{
						return objectMapper.readValue(json, PlaceDetailResponse.class);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			})
			.filter(Objects::nonNull) // null 필터링
			.collect(Collectors.toList());
	}
}
