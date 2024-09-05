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
}
