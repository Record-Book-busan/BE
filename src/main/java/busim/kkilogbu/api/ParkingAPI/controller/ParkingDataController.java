package busim.kkilogbu.api.ParkingAPI.controller;

import java.util.List;

import busim.kkilogbu.api.ParkingAPI.domain.dto.ParkingDataResponse;
import busim.kkilogbu.api.ParkingAPI.service.ParkingDataService;

import busim.kkilogbu.api.restroomAPI.domain.dto.ToiletDataResponse;
import busim.kkilogbu.global.redis.RedisService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingDataController {


    private final ParkingDataService parkingDataService;
    private final RedisService redisService;
   // private final ParkingLocationService parkingLocationService;

    @GetMapping("/nearby")
    public void getNearbyParking(@RequestParam(name = "latitude") double latitude, @RequestParam(name = "longitude") double longitude) {
        log.info("위도: " + latitude + ", 경도: " + longitude);
      //  List<ParkingData> parkingData = parkingLocationService.findParkingWithinRadius(latitude, longitude);
    //    return new ResponseEntity<>(parkingData, HttpStatus.OK);
    }

    @GetMapping("/fetch-and-save")
    public ResponseEntity<String> fetchAndSaveParkingData() {
        try {
            parkingDataService.fetchAndSaveData();
            return new ResponseEntity<>("배치 작업이 성공적으로 실행되었습니다.", HttpStatus.OK);
        } catch (Exception e) {

            throw new RuntimeException(String.format("배치 작업 실행 중 오류 발생: %s", e.getMessage()), e); // 수정된 부분
        }
    }
    @PostMapping("/upload/redis")
    public ResponseEntity<String> uploadInRedis(){
        redisService.saveParkDataInRedis();
        return ResponseEntity.ok("ok");
    }

    @GetMapping
    public ResponseEntity<List<ParkingDataResponse>> getNearToilets(@PathParam("latitude") double lat,
        @PathParam("longitude") double lng, @PathParam("radius") double radius) {
        return ResponseEntity.ok(redisService.getPublicPlacesList(lat, lng, radius, ParkingDataResponse.class));
    }
}
