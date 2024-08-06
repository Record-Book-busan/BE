package busim.kkilogbu.api.ParkingAPI.controller;

import busim.kkilogbu.api.ParkingAPI.service.ParkingDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingDataController {

    private final JobLauncher jobLauncher;
    private final Job simpleJob1;
    private final ParkingDataService parkingDataService;

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