package busim.kkilogbu.record.dto;

import busim.kkilogbu.global.Category1;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "기록 생성 요청 데이터 객체")
public class CreateRecordRequest {

	/*@Schema(description = "1차 카테고리", required = true, example = "PLACE")
	@NotNull(message = "DataCategory 1 is required")
	private Category1 cat1;

	@Schema(description = "2차 카테고리 ID", required = true, example = "2")
	@NotNull(message = "DataCategory 2 is required")
	private Long cat2;
*/

	@Schema(description = "위도", example = "37.5665")
	@NotNull(message = "Latitude is required")
	private Double lat;

	@Schema(description = "경도",  example = "126.9780")
	@NotNull(message = "Longitude is required")
	private Double lng;

	@Schema(description = "기록의 제목",  example = "서울의 유명한 랜드마크")
	@NotNull(message = "Title is required")
	private String title;

	@Schema(description = "기록의 내용",  example = "이곳은 서울의 중심에 위치한 유명한 랜드마크입니다.")
	@NotNull(message = "Content is required")
	private String content;

	@Schema(description = "기록 이미지의 URL", example = "https://example.com/image.jpg")
	@NotNull(message = "Image URL is required")
	private String imageUrl;

	@Schema(description = "기록의 주소", example = "서울특별시 중구 명동")
	@NotNull(message = "Address is required")
	private String address;

}
