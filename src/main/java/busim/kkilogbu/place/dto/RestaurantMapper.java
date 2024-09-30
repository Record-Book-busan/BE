package busim.kkilogbu.place.dto;

import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantMapper {

    public static RestaurantBottomBarResponseDto toRestaurantBottomBarResponseDto(Restaurant restaurant) {
        return RestaurantBottomBarResponseDto.builder()
                .id(restaurant.getId())
                .title(restaurant.getTitle())
                .categories(restaurant.getCategories())
                .detailedInformation(restaurant.getDetailedInformation())
                .build();
    }



    public static List<RestaurantBottomBarResponseDto> toRestaurantBottomBarResponseDtoList(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantMapper::toRestaurantBottomBarResponseDto)
                .collect(Collectors.toList());
    }
}
