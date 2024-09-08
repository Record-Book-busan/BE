package busim.kkilogbu.global.converter;

import busim.kkilogbu.global.Ex.InvalidCategoryException;
import busim.kkilogbu.place.dto.TouristCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTouristCategoryConverter  implements Converter<String, TouristCategory> {

    @Override
    public TouristCategory convert(String source) {
        try {
            return TouristCategory.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryException("유효하지 않은 관광 카테고리 값입니다: " + source);
        }
    }

}
