package busim.kkilogbu.place.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlaceDetailResponse {
	private Long id;
	private String title;
	private String content;
	private List<String> imageUrl;
	private String imageUrl2;
	private String address;
	private String addressDetail;
	private String zipcode;
	private double lat;
	private double lng;
	private String cat1;
	private String cat2;
	private String operatingTime;
	private String phone;
	// TODO : 북마크 여부?
}
