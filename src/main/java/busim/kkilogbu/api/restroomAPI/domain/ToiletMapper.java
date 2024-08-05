package busim.kkilogbu.api.restroomAPI.domain;

import busim.kkilogbu.api.restroomAPI.domain.dto.ToiletDataResponse;
import busim.kkilogbu.api.restroomAPI.domain.entity.ToiletData;

public class ToiletMapper {

    public static ToiletDataResponse toToiletData(ToiletData toiletData){
        return ToiletDataResponse.builder()
                .toiletName(toiletData.getToiletName())
                .latitude(toiletData.getLatitude())
                .longitude(toiletData.getLongitude())
                .openingHours(toiletData.getOpeningHours())
                .phoneNumber(toiletData.getPhoneNumber())
                .build();
    }
}
