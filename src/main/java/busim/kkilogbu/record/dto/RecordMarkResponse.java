package busim.kkilogbu.record.dto;

import static lombok.AccessLevel.*;

import busim.kkilogbu.global.Category1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Schema(description = "기록 마커에 대한 응답 데이터 객체")
public class RecordMarkResponse {

	@Schema(description = "기록의 고유 ID", example = "1")
	private Long id;

	@Schema(description = "기록 이미지의 URL", example = "https://example.com/image.jpg")
	private String imageUrl;

	@Schema(description = "기록의 위도", example = "37.5665")
	private double lat;

	@Schema(description = "기록의 경도", example = "126.9780")
	private double lng;

	@Schema(description = "1차 카테고리", example = "PLACE")
	private Category1 cat1;

	@Schema(description = "2차 카테고리 ID", example = "2")
	private Long cat2;
}

