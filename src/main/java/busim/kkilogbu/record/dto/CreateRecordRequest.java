package busim.kkilogbu.record.dto;

import busim.kkilogbu.global.Category1;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateRecordRequest {
	@NotNull(message = "Category 1 is required")
	private Category1 cat1;
	@NotNull(message = "Category 2 is required")
	private Long cat2;
	@NotNull(message = "Latitude is required")
	private Double lat;
	@NotNull(message = "Longitude is required")
	private Double lng;
	@NotNull(message = "Zipcode is required")
	private String zipcode;
	@NotNull(message = "Address is required")
	private String address;
	@NotNull(message = "Address Detail is required")
	private String addressDetail;
	@NotNull(message = "Title is required")
	private String title;
	@NotNull(message = "Content is required")
	private String content;
	@NotNull(message = "Image URL is required")
	private String imageUrl;
}
