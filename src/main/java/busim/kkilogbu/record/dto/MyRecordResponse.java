package busim.kkilogbu.record.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class MyRecordResponse {
	private Long id;
	private String imageUrl;
	private String title;
	private String content;
	//private Long cat2;
	private double lat;
	private double lng;
}
