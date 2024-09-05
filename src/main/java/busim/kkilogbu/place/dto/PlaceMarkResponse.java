package busim.kkilogbu.place.dto;

import static lombok.AccessLevel.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "장소 마커 응답 데이터")
public class PlaceMarkResponse {

	@Schema(description = "장소 ID", example = "123")
	private Long id;

	@Schema(description = "위도", example = "37.5665")
	private Double lat;        // 위도

	@Schema(description = "경도", example = "126.9780")
	private Double lng;        // 경도

	@Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
	private String imageUrl;   // 이미지 URL

	@Schema(description = "대분류 카테고리", example = "맛집 or 관광")
	private String category;   // 대분류 카테고리
}
