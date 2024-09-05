package busim.kkilogbu.api.restaurantAPI.domain.dto;

import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;
import busim.kkilogbu.place.dto.SearchResultResponse;

public class RestaurantMapper {

    public static RestaurantResponseDto toRestaurantResponseDto(Restaurant restaurant) {
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
                .type(restaurant.getType())
                .build();
    }

    public static SearchResultResponse toSearchResultResponse(Restaurant restaurant) {
        return SearchResultResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getRestaurantName())
                .address(restaurant.getAddress())  // 맛집은 주소를 사용
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .imageUrl(restaurant.getImageUrls().isEmpty() ? null : restaurant.getImageUrls().get(0))  // 첫 번째 이미지를 사용
                .category(restaurant.getType().getDescription())  // 예시로 "맛집" 카테고리를 사용
                .build();
    }
}
