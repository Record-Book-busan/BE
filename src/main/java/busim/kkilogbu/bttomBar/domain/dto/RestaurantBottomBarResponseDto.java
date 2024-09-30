package busim.kkilogbu.bttomBar.domain.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RestaurantBottomBarResponseDto {
    private Long id;                     // 식당 ID
    private String title;                // 제목
    private List<String> categories;     // 카테고리 리스트
    private String detailedInformation;  // 상세 정보
}