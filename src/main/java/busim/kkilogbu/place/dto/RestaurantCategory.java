package busim.kkilogbu.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RestaurantCategory {

    NORMAL_RESTAURANT("일반 맛집", 32L),
    SPECIAL_RESTAURANT("특별 맛집", 64L);

    private final String description;
    private final Long id;

    // 숫자를 받아서 RestaurantCategory로 변환하는 메서드
    public static RestaurantCategory fromType(String type) {
        switch (type) {
            case "1":
                return SPECIAL_RESTAURANT;
            case "0":
                return NORMAL_RESTAURANT;
            default:
                throw new IllegalArgumentException("유효하지 않은 유형 값입니다: " + type);
        }
    }

}
