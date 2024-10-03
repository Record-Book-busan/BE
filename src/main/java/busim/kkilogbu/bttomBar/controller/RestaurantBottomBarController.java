package busim.kkilogbu.bttomBar.controller;


import busim.kkilogbu.bttomBar.domain.dto.CategoryType;
import busim.kkilogbu.bttomBar.domain.dto.RestaurantBottomBarResponseDto;
import busim.kkilogbu.bttomBar.service.BottomBarRestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/kki/place/bottom-bar")
@RequiredArgsConstructor
public class RestaurantBottomBarController {

    private final BottomBarRestaurantService restaurantService;


    // 페이징 및 카테고리 필터링 (디폴트로 TOURISM_RECOMMENDED 사용)
    @GetMapping
    public ResponseEntity<List<RestaurantBottomBarResponseDto>> getRestaurants(
            @RequestParam(required = false, defaultValue = "0", name = "offset") Integer offset,    // 디폴트 offset 0
            @RequestParam(required = false, defaultValue = "10", name = "limit") Integer limit,     // 디폴트 limit 10
            @RequestParam(required = false, defaultValue = "TOURISM_RECOMMENDED", name = "category") CategoryType category // 디폴트 카테고리: TOURISM_RECOMMENDED
    ) {

        log.info("바텀바 조회를 시작 합니다. 카테고리 : " + category);
        // 서비스 레이어로 페이지, 크기, 카테고리 설명을 전달하여 식당 목록을 받아옴
        List<RestaurantBottomBarResponseDto> restaurants = restaurantService.getRestaurants(offset, limit, category);
        return ResponseEntity.ok(restaurants);  // 받은 데이터를 클라이언트에게 반환
    }
}
