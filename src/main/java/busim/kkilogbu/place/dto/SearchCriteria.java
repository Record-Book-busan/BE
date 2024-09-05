package busim.kkilogbu.place.dto;

import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCriteria {

    private String query;          // 검색어
    private Double latitude;       // 검색 중심 좌표 (위도)
    private Double longitude;      // 검색 중심 좌표 (경도)


}
