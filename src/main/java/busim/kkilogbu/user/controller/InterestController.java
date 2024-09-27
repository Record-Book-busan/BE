package busim.kkilogbu.user.controller;


import busim.kkilogbu.security.domain.CustomUserDetails;
import busim.kkilogbu.user.dto.InterestRequest;
import busim.kkilogbu.user.service.InterestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kkilogbu/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;
    private InterestRequest interestRequest;

    /**
     *
     *
     * @param interestRequests
     * @param allSkip
     * @return
     */

    @PostMapping("/my")
    public ResponseEntity<String> saveInterests(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody InterestRequest interestRequests,
            @RequestParam(required = false) Boolean allSkip) {

        // allSkip이 true인 경우 요청을 건너뜁니다.
        if (Boolean.TRUE.equals(allSkip)) {
            return ResponseEntity.ok("관심사 저장이 건너뛰어졌습니다.");
        }

        try {
            interestService.saveUserInterests(customUserDetails.getUsername(),  interestRequest.convertRestaurantCategoriesToString(interestRequests.getRestaurantCategories()), interestRequest.convertTouristCategoriesToString(interestRequests.getTouristCategories())
           );
            return ResponseEntity.ok("관심사가 저장되었습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관심사 저장 중 오류가 발생했습니다.");
        }
    }

    // 2. 관심사 조회 API
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, List<String>>> getUserInterests(@PathVariable Long userId) {
        try {
            Map<String, List<String>> userInterests = interestService.getUserInterests(userId);
            return ResponseEntity.ok(userInterests);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
