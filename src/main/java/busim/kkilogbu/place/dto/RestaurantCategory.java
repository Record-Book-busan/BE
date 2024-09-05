package busim.kkilogbu.place.dto;

public enum RestaurantCategory {

    NORMAL_RESTAURANT("일반 맛집"),
    SPECIAL_RESTAURANT("특별 맛집");

    private final String description;

    RestaurantCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // 숫자를 받아서 RestaurantCategory로 변환하는 메서드
    public static RestaurantCategory fromType(int type) {
        switch (type) {
            case 1:
                return SPECIAL_RESTAURANT;
            case 0:
                return NORMAL_RESTAURANT;
            default:
                throw new IllegalArgumentException("유효하지 않은 유형 값입니다: " + type);
        }
    }

}
