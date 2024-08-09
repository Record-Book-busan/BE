//package busim.kkilogbu.api.ParkingAPI.service;
//
//import busim.kkilogbu.api.ParkingAPI.domain.entity.ParkingData;
//import busim.kkilogbu.api.ParkingAPI.repository.ParkingRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.geo.*;
//import org.springframework.data.redis.connection.RedisGeoCommands;
//import org.springframework.data.redis.core.GeoOperations;
//
//
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ParkingLocationService {
//
//    private final ParkingRepository parkingRepository;
//    private final static String GEO_KEY = "parkingGeo";
//    private final static String CACHE_PREFIX = "parkingCache::";
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    @PostConstruct
//    public void init() {
//        List<ParkingData> allParkingData = parkingRepository.findAll();
//        GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
//        for (ParkingData parkingData : allParkingData) {
//            double longitude = parkingData.getYCdnt(); // yCdnt는 경도
//            double latitude = parkingData.getXCdnt(); // xCdnt는 위도
//
//            if (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180) {
//                geoOps.add(GEO_KEY, new Point(longitude, latitude), parkingData.getId().toString());
//                log.info("Added to geo: ID={} 위도={}, 경도={}", parkingData.getId(), latitude, longitude);
//
//                // 저장된 데이터 확인
//                List<Point> points = geoOps.position(GEO_KEY, parkingData.getId().toString());
//                if (points != null && !points.isEmpty()) {
//                    Point point = points.get(0);
//                    log.info("Verified geo data: ID={} 저장된 위도={}, 저장된 경도={}", parkingData.getId(), point.getY(), point.getX());
//                } else {
//                    log.warn("Failed to verify geo data: ID={}", parkingData.getId());
//                }
//            } else {
//                log.warn("주차 데이터 ID: {}의 위도 또는 경도 값이 유효하지 않습니다.", parkingData.getId());
//            }
//        }
//    }
//
//    public List<ParkingData> findParkingWithinRadius(double latitude, double longitude) {
//        // 캐시 키 생성
//        String cacheKey = CACHE_PREFIX + latitude + ":" + longitude;
//        log.info("Cache key: {}", cacheKey);
//
//        // 캐시에서 데이터 조회
//        List<ParkingData> cachedParkingData = (List<ParkingData>) redisTemplate.opsForValue().get(cacheKey);
//        log.info("Cached parking data: {}", cachedParkingData);
//
//        if (cachedParkingData != null && !cachedParkingData.isEmpty()) {
//            log.info("Returning cached data.");
//            return cachedParkingData;
//        } else {
//            // GeoOperations를 사용하여 Redis에서 지리적 데이터 조회
//            GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
//            Circle withinCircle = new Circle(new Point(longitude, latitude), new Distance(5, Metrics.KILOMETERS));
//            List<RedisGeoCommands.GeoLocation<Object>> geoLocations = geoOps.radius(GEO_KEY, withinCircle).getContent()
//                    .stream()
//                    .map(GeoResult::getContent)
//                    .collect(Collectors.toList());
//
//            log.info("geo 에서 조회하기 geoLocations : " + geoLocations);
//
//            // 조회된 데이터를 사용하여 주차 데이터 ID 리스트 생성
//            List<String> parkingIds = geoLocations.stream()
//                    .filter(geoLocation -> geoLocation.getPoint() != null)
//                    .map(geoLocation -> geoLocation.getName().toString())
//                    .collect(Collectors.toList());
//
//            log.info("Parking IDs within radius: {}", parkingIds);
//
//            // 주차 데이터 ID 리스트를 사용하여 데이터베이스에서 주차 데이터 조회
//        //    if (!parkingIds.isEmpty()) {
//                List<ParkingData> parkingData = parkingRepository.findAllById(parkingIds.stream().map(Long::valueOf).collect(Collectors.toList()));
//                log.info("Parking data fetched from DB: {}", parkingData);
//
//                // 조회한 데이터를 캐시에 저장
//                redisTemplate.opsForValue().set(cacheKey, parkingData, 1, TimeUnit.HOURS); // 캐시 만료 시간 1시간
//                log.info("Cached new parking data.");
//                return parkingData;
//
//                //log.info("No parking data found within the specified radius.");
//             //   return Collections.emptyList();
//
//        }
//    }
//
//}
