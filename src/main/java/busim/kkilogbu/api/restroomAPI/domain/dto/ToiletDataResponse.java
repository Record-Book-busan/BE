package busim.kkilogbu.api.restroomAPI.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ToiletDataResponse {

    private String toiletName; // 화장실명
    private double latitude; // 위도
    private double longitude; // 경도
    private String phoneNumber; // 전화번호
    private String openingHours; // 개방 시간
}
