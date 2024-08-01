package busim.kkilogbu.record.dto;

import java.time.LocalDateTime;

import busim.kkilogbu.global.Category1;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InputRecordResponse {
	private Long id;
	private Category1 cat1;
	private Long cat2;
	private double lat;
	private double lng;
	private String zipcode;
	private String address;
	private String addressDetail;
	private String title;
	private String content;
	private String imageUrl;
	private LocalDateTime currentTime;
}
