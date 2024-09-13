package busim.kkilogbu.api.restaurantAPI.domain.dto;

import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;
import busim.kkilogbu.place.dto.RestaurantCategory;
import busim.kkilogbu.place.dto.SearchResultResponse;

public class RestaurantMapper {

    public static RestaurantResponseDto toRestaurantResponseDto(Restaurant restaurant) {
        String image  = "";
        if(restaurant.getImageUrls().get(0).startsWith("[\'")){
            image = restaurant.getImageUrls().get(0).substring(2, restaurant.getImageUrls().get(0).length()-2);
        }
        return RestaurantResponseDto.builder()
                .id(restaurant.getId())
                .title(restaurant.getTitle())
                .address(restaurant.getAddress())
                .landLotAddress(restaurant.getLandLotAddress())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .phoneNumber(restaurant.getPhoneNumber())
                .businessType(restaurant.getBusinessType())
                .detailedInformation(restaurant.getDetailedInformation())
                .imageUrl(image)
                .restaurantName(restaurant.getRestaurantName())
                .categories(restaurant.getCategories()) // 카테고리 리스트를 DTO로 변환
                .type(restaurant.getType())
                .build();
    }

    public static SearchResultResponse toSearchResultResponse(Restaurant restaurant) {
        return SearchResultResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getTitle())
                .address(restaurant.getAddress())  // 맛집은 주소를 사용
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .imageUrl(
                restaurant.getImageUrls().isEmpty() || "준비중입니다".equals(restaurant.getImageUrls().get(0))
                        ? "" : restaurant.getImageUrls().get(0))  // 이미지가 "준비중입니다"이면 빈 문자열 반환
                .category(RestaurantCategory.fromType(restaurant.getType()).getDescription())  // 예시로 "맛집" 카테고리를 사용
                .build();
    }
}
