package busim.kkilogbu.place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDetailResponse {

	private Long id;
	private String title;
	private String content;
	private List<String> imageUrl;
	private String imageUrl2;
	private String address;
	private double lat;
	private double lng;
	private String cat1;
	private TouristCategory touristCat2;
	private List<String> restaurantCat2;
	private String report;
	// TODO : 북마크 여부?
}
