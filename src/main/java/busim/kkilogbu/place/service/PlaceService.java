package busim.kkilogbu.place.service;

import busim.kkilogbu.api.restaurantAPI.domain.dto.RestaurantMapper;
import busim.kkilogbu.api.restaurantAPI.domain.dto.RestaurantResponseDto;

import busim.kkilogbu.api.restaurantAPI.repository.RestaurantRepository;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristMapper;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristResponseDto;

import busim.kkilogbu.api.touristAPI.repository.TouristRepository;
import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.place.dto.RestaurantCategory;
import busim.kkilogbu.place.dto.TouristCategory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import busim.kkilogbu.place.dto.PlaceDetailResponse;

import busim.kkilogbu.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class PlaceService {


    private final PlaceRepository placeRepository;

    private final RestaurantRepository restaurantRepository;
    private final TouristRepository touristRepository;


    public PlaceDetailResponse getRestaurantDetail(Long placeId) {
        var restaurant = restaurantRepository.findById(placeId)
                .orElseThrow(() -> new BaseException("맛집을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        return RestaurantMapper.toPlaceDetailResponse(restaurant);
    }

    public PlaceDetailResponse getTouristDetail(Long placeId) {
        var tourist = touristRepository.findById(placeId)
                .orElseThrow(() -> new BaseException("관광지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        return TouristMapper.toPlaceDetailResponse(tourist);
    }

}
