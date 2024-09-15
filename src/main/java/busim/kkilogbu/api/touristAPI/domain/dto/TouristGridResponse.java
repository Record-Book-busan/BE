package busim.kkilogbu.api.touristAPI.domain.dto;

import busim.kkilogbu.place.dto.TouristCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TouristGridResponse {

    private Long id;
    private String title;
    private String thumbnailUrl;
    private String address;
    private TouristCategory touristCategory;
    private double lat;
    private double lng;
}
