package busim.kkilogbu.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "내 기록 응답 데이터")
public class MyRecordResponse {

	@Schema(description = "기록 ID", example = "123")
	private Long id;

	@Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
	private String imageUrl;

	@Schema(description = "제목", example = "서울 여행")
	private String title;

	@Schema(description = "내용", example = "서울 여행 기록")
	private String content;

	@Schema(description = "위도", example = "37.5665")
	private double lat;

	@Schema(description = "경도", example = "126.9780")
	private double lng;
}
