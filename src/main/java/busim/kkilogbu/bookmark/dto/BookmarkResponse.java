package busim.kkilogbu.bookmark.dto;

import static lombok.AccessLevel.*;

import busim.kkilogbu.global.Category1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class BookmarkResponse {
	private Long id;
	private String title;
	private String address;
	private Category1 cat1;
	private Long cat2;

}
