package busim.kkilogbu.place.service;


import busim.kkilogbu.api.restaurantAPI.repository.RestaurantRepository;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristMapper;
import busim.kkilogbu.api.touristAPI.domain.entity.Tourist;
import busim.kkilogbu.api.touristAPI.repository.TouristRepository;
import busim.kkilogbu.bookmark.service.BookmarkService;
import busim.kkilogbu.place.dto.SearchResultResponse;
import busim.kkilogbu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RestaurantRepository restaurantRepository;
    private final TouristRepository touristRepository;
    private final BookmarkService bookmarkService;


    public Page<SearchResultResponse> search(String query, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        // TODO : 로그인 구현시 변경
        User user = User.builder().nickname("tester").build();

        // 관광지 페이징 검색 결과
        Page<Tourist> tourists = touristRepository.findByMultipleFields(query, pageable);

        // 검색 결과를 SearchResultResponse로 변환
        List<SearchResultResponse> touristResults = tourists.stream()
            .map(tourist -> {
                boolean bookMarked = bookmarkService.isBookMarked(user, tourist.getPlace());
                return TouristMapper.toSearchResultResponse(tourist, bookMarked);
            })
            .collect(Collectors.toList());

        return new PageImpl<>(touristResults, pageable, tourists.getTotalElements());
    }
}
