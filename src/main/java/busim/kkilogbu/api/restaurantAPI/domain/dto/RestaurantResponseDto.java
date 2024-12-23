package busim.kkilogbu.api.restaurantAPI.domain.dto;

import busim.kkilogbu.place.dto.RestaurantCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RestaurantResponseDto {

    private Long id;
    private String title;
    private String address;
    private String landLotAddress;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private String businessType;
    private String detailedInformation;
    private String imageUrl;
    private String restaurantName;
    private List<String> categories;
    private String type;

}

