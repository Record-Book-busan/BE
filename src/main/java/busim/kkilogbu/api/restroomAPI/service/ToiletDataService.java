package busim.kkilogbu.api.restroomAPI.service;

import busim.kkilogbu.api.restroomAPI.domain.dto.ToiletDataResponse;

import java.util.List;

public interface ToiletDataService {

    public List<ToiletDataResponse> getToiletsWithinRadius(double latitude, double longitude);
}
