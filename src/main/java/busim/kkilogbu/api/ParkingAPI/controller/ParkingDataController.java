package busim.kkilogbu.api.ParkingAPI.controller;

import busim.kkilogbu.api.ParkingAPI.domain.entity.ParkingData;
import busim.kkilogbu.api.ParkingAPI.service.ParkingDataService;

import busim.kkilogbu.api.ParkingAPI.service.ParkingLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingDataController {


    private final ParkingDataService parkingDataService;

    private final ParkingLocationService parkingLocationService;

    @GetMapping("/nearby")
    public ResponseEntity<List<ParkingData>> getNearbyParking(@RequestParam(name = "latitude") double latitude, @RequestParam(name = "longitude") double longitude) {
        log.info("위도: " + latitude + ", 경도: " + longitude);
        List<ParkingData> parkingData = parkingLocationService.findParkingWithinRadius(latitude, longitude);
        return new ResponseEntity<>(parkingData, HttpStatus.OK);
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
}