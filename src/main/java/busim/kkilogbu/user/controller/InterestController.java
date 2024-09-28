package busim.kkilogbu.user.controller;


import busim.kkilogbu.security.domain.CustomUserDetails;
import busim.kkilogbu.user.dto.InterestRequest;
import busim.kkilogbu.user.service.InterestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
            @RequestParam(name = "allSkip", required = false) Boolean allSkip
    ) {
        log.info("유저의 관심사 저장 요청을 받았습니다: {}", customUserDetails.getUsername());

        // allSkip이 true인 경우 요청을 건너뜁니다.
        if (Boolean.TRUE.equals(allSkip)) {
            log.info("'allSkip' 값이 true여서 관심사 저장을 건너뜁니다. 유저: {}", customUserDetails.getUsername());
            return ResponseEntity.ok("관심사 저장이 건너뛰어졌습니다.");
        }

        try {
            log.info("유저의 관심사를 저장 중입니다: {}", customUserDetails.getUsername());

            // 카테고리 변환
            List<String> restaurantCategories = interestRequests.convertRestaurantCategoriesToString(interestRequests.getRestaurantCategories());
            List<String> touristCategories = interestRequests.convertTouristCategoriesToString(interestRequests.getTouristCategories());

            log.debug("레스토랑 카테고리: {}", restaurantCategories);
            log.debug("관광 카테고리: {}", touristCategories);

            // 관심사 저장
            interestService.saveUserInterests(customUserDetails.getUsername(), restaurantCategories, touristCategories);

            log.info("유저의 관심사가 성공적으로 저장되었습니다: {}", customUserDetails.getUsername());
            return ResponseEntity.ok("관심사가 저장되었습니다.");
        } catch (EntityNotFoundException e) {
            log.error("유저를 찾을 수 없습니다: {}", customUserDetails.getUsername(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다: " + e.getMessage());
        } catch (Exception e) {
            log.error("유저의 관심사 저장 중 오류가 발생했습니다: {}", customUserDetails.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관심사 저장 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/my/get")
    public ResponseEntity<Map<String, Object>> getUserInterests(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            Map<String, Object> userInterests = interestService.getUserInterests(customUserDetails.getUsername());

            // 관심사가 없을 때 처리
            if (userInterests.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "관심사가 없습니다");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            // 관심사가 있을 경우
            return ResponseEntity.ok(userInterests);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



}
