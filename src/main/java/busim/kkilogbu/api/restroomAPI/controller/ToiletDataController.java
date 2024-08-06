package busim.kkilogbu.api.restroomAPI.controller;


import busim.kkilogbu.api.restroomAPI.domain.dto.ToiletDataResponse;
import busim.kkilogbu.api.restroomAPI.service.ToiletDataService;
import busim.kkilogbu.api.restroomAPI.service.ToiletDataServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/toiletData")
@RequiredArgsConstructor
public class ToiletDataController {

    private final ToiletDataServiceImpl toiletDataServiceImpl;
    private final ToiletDataService toiletDataService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CSV 파일을 선택해주세요.");
        }

        toiletDataServiceImpl.saveCSVData(file);
        return ResponseEntity.status(HttpStatus.OK).body("CSV 파일이 성공적으로 업로드되었습니다.");
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<ToiletDataResponse>> getNearbyToilets(@RequestParam(name = "latitude") double latitude, @RequestParam(name = "longitude") double longitude) {
        List<ToiletDataResponse> toilets = toiletDataService.getToiletsWithinRadius(latitude, longitude);
        return ResponseEntity.ok(toilets);
    }


}