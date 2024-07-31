package busim.kkilogbu.place.dto;

import busim.kkilogbu.global.Category1;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDetailResponse {
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
	private String operatingTime;
	private String phone;
	// TODO : 북마크 여부?
}
