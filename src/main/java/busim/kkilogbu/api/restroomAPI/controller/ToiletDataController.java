package busim.kkilogbu.api.restroomAPI.controller;


import busim.kkilogbu.api.restroomAPI.service.ToiletDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/toiletData")
@RequiredArgsConstructor
public class ToiletDataController {

    private final ToiletDataService toiletDataService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CSV 파일을 선택해주세요.");
        }

        toiletDataService.saveCSVData(file);
        return ResponseEntity.status(HttpStatus.OK).body("CSV 파일이 성공적으로 업로드되었습니다.");
    }
}