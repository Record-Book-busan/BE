package busim.kkilogbu.api.restaurantAPI.domain.dto;

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
    private String detailedInformation;
    private List<String> imageUrls;
    private String restaurantName;
    private List<String> categories; // 카테고리 필드를 리스트로 변경

}
