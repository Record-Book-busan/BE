package busim.kkilogbu.bttomBar.service;

import busim.kkilogbu.api.restaurantAPI.domain.dto.RestaurantMapper;
import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;
import busim.kkilogbu.api.restaurantAPI.repository.RestaurantRepository;
import busim.kkilogbu.bttomBar.domain.dto.CategoryType;
import busim.kkilogbu.bttomBar.domain.dto.RestaurantBottomBarResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BottomBarRestaurantService {

    private final RestaurantRepository restaurantRepository;


    public List<RestaurantBottomBarResponseDto> getRestaurants(Integer offset, Integer limit, CategoryType category) {
        // offset과 limit을 사용해 PageRequest 생성 (offset / limit = pageNumber)
        Pageable pageable = PageRequest.of(offset / limit, limit);

        List<Restaurant> restaurants;
        log.info("서비스 카테고리: " + category.getDescription());

        // 카테고리가 ALL일 경우는 findAll을 사용하고, 그 외에는 카테고리를 필터링
        if (category == CategoryType.ALL) {
            restaurants = restaurantRepository.findAll(pageable).getContent();
        } else {
            restaurants = restaurantRepository.findByCategory(category.getDescription(), pageable);
        }

        log.info("가져온 음식점 목록: " + restaurants);

        // 엔티티 리스트를 DTO 리스트로 변환하여 반환
        return RestaurantMapper.toRestaurantBottomBarResponseDtoList(restaurants);
    }
}
