package busim.kkilogbu.place.dto;

import static lombok.AccessLevel.*;

import busim.kkilogbu.global.Category1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceMarkResponse {

	private Long id;
	private Double lat;        // 위도
	private Double lng;        // 경도
	private String imageUrl;   // 이미지 URL
	private String category;   // 대분류 카테고리

}

