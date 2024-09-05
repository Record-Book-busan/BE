package busim.kkilogbu.api.restaurantAPI.domain.dto;

import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;

public class RestaurantMapper {

    public static RestaurantResponseDto toDto(Restaurant restaurant) {
        return RestaurantResponseDto.builder()
                .title(restaurant.getTitle())
                .address(restaurant.getAddress())
                .landLotAddress(restaurant.getLandLotAddress())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .phoneNumber(restaurant.getPhoneNumber())
                .businessType(restaurant.getBusinessType())
                .detailedInformation(restaurant.getDetailedInformation())
                .imageUrls(restaurant.getImageUrls())
                .restaurantName(restaurant.getRestaurantName())
                .categories(restaurant.getCategories()) // 카테고리 리스트를 DTO로 변환
                .build();
    }
}
