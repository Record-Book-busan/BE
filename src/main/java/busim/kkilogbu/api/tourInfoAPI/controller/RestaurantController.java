package busim.kkilogbu.api.tourInfoAPI.controller;

import busim.kkilogbu.api.tourInfoAPI.service.RestaurantService;
import busim.kkilogbu.api.tourInfoAPI.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/kkilogbu/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsv(@RequestParam(name ="file") MultipartFile file) {
        try {
            restaurantService.saveCSVData(file);
            return ResponseEntity.ok("CSV 파일이 성공적으로 처리되었고, 데이터가 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CSV 파일을 처리하는 동안 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
