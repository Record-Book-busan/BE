package busim.kkilogbu.record.dto;

import busim.kkilogbu.global.Category1;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "기록의 상세 정보에 대한 응답 데이터 객체")
public class RecordDetailResponse {

	@Schema(description = "기록의 고유 ID", example = "1")
	private Long id;

	@Schema(description = "기록의 제목", example = "서울의 유명한 랜드마크")
	private String title;

	@Schema(description = "기록의 내용", example = "이곳은 서울의 중심에 위치한 유명한 랜드마크입니다.")
	private String content;

	@Schema(description = "기록 이미지의 URL", example = "https://example.com/image.jpg")
	private String imageUrl;


	@Schema(description = "기록의 위도", example = "37.5665")
	private double lat;

	@Schema(description = "기록의 경도", example = "126.9780")
	private double lng;


	@Schema(description = "기록 생성 시간", example = "2023-09-01T12:00:00")
	private String createdAt;

	@Schema(description = "기록의 주소", example = "서울특별시 중구 명동")
	private String address;
}
