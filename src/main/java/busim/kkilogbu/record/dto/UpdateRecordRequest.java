package busim.kkilogbu.record.dto;

import busim.kkilogbu.global.Category1;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateRecordRequest {
	// TODO : 일단 수정할때도 모두 입력받게 해놨는데 논의 필요

	@NotNull(message = "Latitude is required")
	private Double lat;
	@NotNull(message = "Longitude is required")
	private Double lng;
	@NotNull(message = "Address is required")
	private String address;
	@NotNull(message = "Title is required")
	private String title;
	@NotNull(message = "Content is required")
	private String content;
	@NotNull(message = "Image URL is required")
	private String imageUrl;
}
