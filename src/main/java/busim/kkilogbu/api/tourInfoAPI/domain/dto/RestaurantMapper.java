package busim.kkilogbu.api.tourInfoAPI.domain.dto;

import busim.kkilogbu.api.tourInfoAPI.domain.entity.Restaurant;

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
                .businessLicenseName(restaurant.getBusinessLicenseName())
                .detailedInformation(restaurant.getDetailedInformation())
                .imageUrls(restaurant.getImageUrls())
                .restaurantName(restaurant.getRestaurantName())
                .category(restaurant.getCategory())
                .build();


    }
}
