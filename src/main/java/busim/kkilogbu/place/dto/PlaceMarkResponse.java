package busim.kkilogbu.place.dto;

import static lombok.AccessLevel.*;

import busim.kkilogbu.global.Category1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PlaceMarkResponse {
	private Long id;
	private String imageUrl;
	private double lat;
	private double lng;
	private Category1 cat1;
	private Long cat2;
}
