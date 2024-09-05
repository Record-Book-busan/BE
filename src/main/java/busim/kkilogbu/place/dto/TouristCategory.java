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

}
