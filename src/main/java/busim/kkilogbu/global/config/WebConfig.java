package busim.kkilogbu.global.config;

import busim.kkilogbu.place.dto.RestaurantCategory;
import busim.kkilogbu.place.dto.TouristCategory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final Converter<String, RestaurantCategory> stringToRestaurantCategoryConverter;
    private final Converter<String, TouristCategory> stringToTouristCategoryConverter;

    public WebConfig(Converter<String, RestaurantCategory> stringToRestaurantCategoryConverter,
                     Converter<String, TouristCategory> stringToTouristCategoryConverter) {
        this.stringToRestaurantCategoryConverter = stringToRestaurantCategoryConverter;
        this.stringToTouristCategoryConverter = stringToTouristCategoryConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToRestaurantCategoryConverter);
        registry.addConverter(stringToTouristCategoryConverter);
    }
}
