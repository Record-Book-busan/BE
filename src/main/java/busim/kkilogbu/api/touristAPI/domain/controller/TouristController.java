package busim.kkilogbu.api.touristAPI.domain.controller;


import busim.kkilogbu.api.touristAPI.service.TouristService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tourist")
@RequiredArgsConstructor
public class TouristController {

    private final TouristService touristService;

    // CSV 파일을 업로드하여 데이터를 저장하는 엔드포인트
    @PostMapping("/upload")
    public ResponseEntity<String> uploadTouristData(@RequestParam("file") MultipartFile file) {
        try {
            touristService.saveTouristsFromCSV(file);
            return ResponseEntity.status(HttpStatus.OK).body("파일이 성공적으로 업로드되고 처리되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}