package busim.kkilogbu.api.ParkingAPI.service;


import busim.kkilogbu.api.ParkingAPI.domain.entity.ParkingData;
import busim.kkilogbu.api.ParkingAPI.repository.ParkingRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingRedisService {

    private static final String GEO_KEY = "parking";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ParkingRepository parkingRepository;

    @PostConstruct
    public void loadParkingData() {
        List<ParkingData> parkingDataList = parkingRepository.findAll();
        log.info("주차장 데이터를 로드합니다. 총 {}건", parkingDataList.size());
        for (ParkingData parkingData : parkingDataList) {
            addParkingLocation(parkingData);
        }
    }

    public void addParkingLocation(ParkingData parkingData) {
        redisTemplate.opsForGeo().add(
                GEO_KEY,
                new RedisGeoCommands.GeoLocation<>(
                        parkingData.getPkNam(),
                        new org.springframework.data.geo.Point(parkingData.getXCdnt(), parkingData.getYCdnt())
                )
        );
        log.info("주차장 위치를 추가했습니다: {}, 경도: {}, 위도: {}", parkingData.getPkNam(), parkingData.getXCdnt(), parkingData.getYCdnt());
    }
//
//    public List<String> getNearbyParkingLocations(double longitude, double latitude, double radiusInKm) {
//        List<String> nearbyParkings = redisTemplate.opsForGeo()
//                .radius(GEO_KEY, new org.springframework.data.geo.Circle(new org.springframework.data.geo.Point(longitude, latitude), radiusInKm * 1000))  // 반경 km를 m로 변환
//                .getContent()
//                .stream()
//                .map(RedisGeoCommands.GeoLocation::getName)
//                .collect(Collectors.toList());
//        log.info("근처 주차장을 검색했습니다. 경도: {}, 위도: {}, 반경: {}km, 결과: {}건", longitude, latitude, radiusInKm, nearbyParkings.size());
//        return nearbyParkings;
//    }
//
//    // 정적 컨텍스트에서 비정적 메서드를 호출하기 위한 정적 메서드 예시
//    public static void initializeAndLoadParkingData() {
//        ParkingRepository parkingRepository = // ParkingRepository 인스턴스를 생성하거나 주입
//        RedisTemplate<String, Object> redisTemplate = // RedisTemplate 인스턴스를 생성하거나 주입
//                ParkingRedisService service = new ParkingRedisService(redisTemplate, parkingRepository);
//        service.loadParkingData();

}