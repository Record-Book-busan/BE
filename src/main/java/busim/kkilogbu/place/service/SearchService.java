package busim.kkilogbu.place.service;

import busim.kkilogbu.api.restaurantAPI.domain.dto.RestaurantMapper;
import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;
import busim.kkilogbu.api.restaurantAPI.repository.RestaurantRepository;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristMapper;
import busim.kkilogbu.api.touristAPI.domain.entity.Tourist;
import busim.kkilogbu.api.touristAPI.repository.TouristRepository;
import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.place.dto.PlaceDetailResponse;
import busim.kkilogbu.place.dto.SearchResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final RestaurantRepository restaurantRepository;
    private final TouristRepository touristRepository;

    public Page<SearchResultResponse> search(String query, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);


        Page<Restaurant> restaurants = restaurantRepository.findByMultipleFields(query, pageable);

        // 관광지 페이징 검색 결과
        Page<Tourist> tourists = touristRepository.findByMultipleFields(query, pageable);

        // 맛집 검색 결과를 SearchResultResponse로 변환
        List<SearchResultResponse> restaurantResults = restaurants.stream()
                .map(restaurant -> RestaurantMapper.toSearchResultResponse(restaurant)) // 명시적으로 메서드 사용
                .collect(Collectors.toList());

       // 관광지 검색 결과를 SearchResultResponse로 변환
        List<SearchResultResponse> touristResults = tourists.stream()
                .map(TouristMapper::toSearchResultResponse) // 기존 Tourist에 대한 변환 메서드 사용
                .collect(Collectors.toList());

// 두 결과를 합쳐서 반환
        List<SearchResultResponse> combinedResults = new ArrayList<>();
        combinedResults.addAll(restaurantResults);
        combinedResults.addAll(touristResults);

// 결과를 페이지로 변환하여 반환
        Page<SearchResultResponse> searchResultsPage = new PageImpl<>(combinedResults, pageable, combinedResults.size());

        return searchResultsPage;
    }

    public PlaceDetailResponse getSearchRestaurantDetail(Long placeId) {
        var restaurant = restaurantRepository.findById(placeId)
                .orElseThrow(() -> new BaseException("맛집을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        return RestaurantMapper.toPlaceDetailResponse(restaurant);
    }

    public PlaceDetailResponse getSearchTouristDetail(Long placeId) {
        var tourist = touristRepository.findById(placeId)
                .orElseThrow(() -> new BaseException("관광지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        return TouristMapper.toPlaceDetailResponse(tourist);
    }

}


