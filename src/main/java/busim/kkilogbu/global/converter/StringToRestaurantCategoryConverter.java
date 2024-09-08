package busim.kkilogbu.global.converter;

import busim.kkilogbu.global.Ex.InvalidCategoryException;
import busim.kkilogbu.place.dto.RestaurantCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToRestaurantCategoryConverter implements Converter<String, RestaurantCategory> {


    @Override
    public RestaurantCategory convert(String source) {
        try {
            return RestaurantCategory.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryException("유효하지 않은 맛집 카테고리 값입니다: " + source);
        }
    }

}
