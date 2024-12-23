package busim.kkilogbu.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "검색 결과 응답 데이터")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResultResponse {

        @Schema(description = "ID", example = "1")
        private Long id;

        private String division;

        @Schema(description = "장소명 (맛집 또는 관광지)", example = "서울 맛집")
        private String name;          // 장소명 (맛집 또는 관광지)

        @Schema(description = "주소 (맛집일 경우 적용)", example = "서울시 강남구")
        private String address;       // 맛집일 경우에만 적용

        @Schema(description = "위도", example = "37.5665")
        private Double latitude;      // 위도

        @Schema(description = "경도", example = "126.9780")
        private Double longitude;     // 경도

        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
        private String imageUrl;      // 이미지 URL

        @Schema(description = "카테고리 (맛집 또는 관광지의 대분류)", example = "맛집")
        private String category;      // 카테고리 (맛집 또는 관광지의 대분류)
}
