package busim.kkilogbu.api.touristAPI.domain.dto;

import busim.kkilogbu.api.touristAPI.domain.entity.Tourist;
import busim.kkilogbu.place.dto.SearchResultResponse;
import busim.kkilogbu.place.dto.TouristCategory;

public class TouristMapper {

    public static TouristResponseDto toTouristResponseDto(Tourist tourist) {
        return TouristResponseDto.builder()
                .id(tourist.getId())
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


    public static SearchResultResponse toSearchResultResponse(Tourist tourist) {
        return SearchResultResponse.builder()
                .id(tourist.getId())
                .name(tourist.getName())
                .address(tourist.getLocation())  // 관광지는 위치를 사용
                .latitude(tourist.getLatitude())
                .longitude(tourist.getLongitude())
                .imageUrl(tourist.getImageUrl())
                .category(TouristCategory.fromString(tourist.getCategoryLarge()).getDescription())  // 관광지의 대분류를 카테고리로 사용
                .build();
    }


}
