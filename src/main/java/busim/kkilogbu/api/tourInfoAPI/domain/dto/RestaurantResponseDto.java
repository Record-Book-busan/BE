package busim.kkilogbu.api.tourInfoAPI.domain.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RestaurantResponseDto {


    private String title;
    private String address;
    private String landLotAddress;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private String businessType;
    private String businessLicenseName;
    private String detailedInformation;
    private List<String> imageUrls;
    private String restaurantName;
    private String category;

}