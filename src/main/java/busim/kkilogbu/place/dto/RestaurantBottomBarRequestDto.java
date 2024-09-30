package busim.kkilogbu.place.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBottomBarRequestDto {

    private Boolean all;         // 전체 항목 조회 여부
    private List<String> categories; // 카테고리 리스트
}
