package busim.kkilogbu.api.touristAPI.domain.dto;


import busim.kkilogbu.place.dto.TouristCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TouristResponseDto {


    private String name;          // 장소명 (장소)
    private String location;      // 위치
    private String imageUrl;      // 이미지 URL
    private Double longitude;     // 경도
    private Double latitude;      // 위도
    private TouristCategory categoryLarge;   // 대분류
    private String categoryMedium;  // 중분류
    private String categorySmall;   // 소분류
}
