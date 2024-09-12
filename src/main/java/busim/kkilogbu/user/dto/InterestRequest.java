package busim.kkilogbu.user.dto;


import busim.kkilogbu.place.dto.RestaurantCategory;
import busim.kkilogbu.place.dto.TouristCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterestRequest {

    private List<TouristCategory> touristCategories;
    private List<RestaurantCategory> restaurantCategories;

    // TouristCategory와 RequestUserCategory를 String으로 변환하는 메소드
    public List<String> convertTouristCategoriesToString(List<TouristCategory> touristCategories) {
        return touristCategories.stream()
                .map(TouristCategory::name)  // enum을 문자열로 변환
                .collect(Collectors.toList());
    }

    public List<String> convertRestaurantCategoriesToString(List<RestaurantCategory> restaurantCategories) {
        return restaurantCategories.stream()
                .map(RestaurantCategory::name)  // enum을 문자열로 변환
                .collect(Collectors.toList());
    }
}
