package busim.kkilogbu.api.touristAPI.domain.dto;

import busim.kkilogbu.api.touristAPI.domain.entity.Tourist;

public class TouristMapper {

    public static TouristResponseDto toTouristResponseDto(Tourist tourist) {
        return TouristResponseDto.builder()
                .name(tourist.getName())
                .location(tourist.getLocation())
                .imageUrl(tourist.getImageUrl())
                .latitude(tourist.getLatitude())
                .longitude(tourist.getLongitude())
                .categoryLarge(tourist.getCategoryLarge())
                .categoryMedium(tourist.getCategoryMedium())
                .categorySmall(tourist.getCategorySmall())
                .build();
    }
}
