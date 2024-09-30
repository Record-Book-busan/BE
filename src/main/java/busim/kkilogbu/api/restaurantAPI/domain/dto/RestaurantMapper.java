package busim.kkilogbu.api.restaurantAPI.domain.dto;

import busim.kkilogbu.api.restaurantAPI.domain.dto.RestaurantResponseDto;
import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;
import busim.kkilogbu.bttomBar.domain.dto.RestaurantBottomBarResponseDto;
import busim.kkilogbu.place.dto.PlaceDetailResponse;
import busim.kkilogbu.place.dto.RestaurantCategory;
import busim.kkilogbu.place.dto.SearchResultResponse;

import java.util.List;
import java.util.stream.Collectors;

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
                .division("맛집")
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

    public static PlaceDetailResponse toPlaceDetailResponse(Restaurant restaurant) {
        return PlaceDetailResponse.builder()
                .id(restaurant.getId())
                .title(restaurant.getRestaurantName()) // 맛집 이름
                .address(restaurant.getAddress()) // 주소
                .lng(restaurant.getLongitude()) // 경도
                .lat(restaurant.getLatitude()) // 위도
                .cat1("restaurant") // 카테고리: 맛집
                .restaurantCat2(restaurant.getCategories()) // 맛집의 세부 카테고리
                .report(restaurant.getDetailedInformation()) // 맛집의 상세 정보
                .imageUrl(restaurant.getImageUrls()) // 이미지 URL 리스트
                .phoneNumber(restaurant.getPhoneNumber())
                .businessType(restaurant.getBusinessType())
                .build();
    }

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
