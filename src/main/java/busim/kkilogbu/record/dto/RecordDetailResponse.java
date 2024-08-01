package busim.kkilogbu.record.dto;

import java.time.LocalDateTime;

import busim.kkilogbu.global.Category1;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RecordDetailResponse {
	private Long id;
	private String title;
	private String content;
	private String imageUrl;
	private String address;
	private String addressDetail;
	private String zipcode;
	private double lat;
	private double lng;
	private Category1 cat1;
	private Long cat2;
	private String nickName;
	private LocalDateTime createdAt;
	// TODO : 북마크 여부?
}
