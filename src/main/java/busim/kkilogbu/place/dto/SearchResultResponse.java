package busim.kkilogbu.place.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResultResponse {

        private Long id;
        private String name;          // 장소명 (맛집 또는 관광지)
        private String address;       // 맛집일 경우에만 적용
        private String location;      // 관광지일 경우에만 적용
        private Double latitude;      // 위도
        private Double longitude;     // 경도
        private String imageUrl;      // 이미지 URL
        private String category;      // 카테고리 (맛집 또는 관광지의 대분류)
    }

