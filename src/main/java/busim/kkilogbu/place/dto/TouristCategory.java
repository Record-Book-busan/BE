package busim.kkilogbu.place.dto;

public enum TouristCategory {

    TOURIST_SPOT("관광지"),
    THEME("테마"),
    HOT_PLACE("핫플"),
    NATURE("자연"),
    LEISURE_SPORTS("레포츠");

    private final String description;

    TouristCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // 문자열을 TouristCategory로 변환하는 메소드
    public static TouristCategory fromString(String category) {
        switch (category) {
            case "레포츠":
                return LEISURE_SPORTS;
            case "인문(문화/예술/역사)":
            case "관광지":
                return TOURIST_SPOT;
            case "자연":
                return NATURE;
            case "숙박":
            case "쇼핑":
                return THEME;
            case "핫플":
                return HOT_PLACE;
            default:
                throw new IllegalArgumentException("해당 카테고리는 존재하지 않습니다: " + category);
        }
    }

}
