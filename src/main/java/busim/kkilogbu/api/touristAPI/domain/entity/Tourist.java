package busim.kkilogbu.api.touristAPI.domain.entity;

import busim.kkilogbu.place.dto.TouristCategory;
import busim.kkilogbu.place.entity.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tourist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;          // 장소명 (장소)
    private String location;      // 위치
    private String imageUrl;      // 이미지 URL
    private Double longitude;     // 경도
    private Double latitude;      // 위도

    private String categoryLarge;   // 대분류
    private String categoryMedium;  // 중분류
    private String categorySmall;   // 소분류


}
