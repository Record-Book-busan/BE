package busim.kkilogbu.place.controller;

import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.place.dto.PlaceDetailResponse;
import busim.kkilogbu.place.dto.SearchDetailResponse;
import busim.kkilogbu.place.dto.SearchResultResponse;
import busim.kkilogbu.place.service.SearchService;
import busim.kkilogbu.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/kkilogbu/place/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;


    @Operation(summary = "검색", description = "검색어를 기반으로 맛집 또는 관광지를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공"),
            @ApiResponse(responseCode = "200", description = "검색 결과가 없음")
    })
    @GetMapping()
    public ResponseEntity<List<SearchResultResponse>> search(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "검색어", example = "맛집") @RequestParam(name = "query") String query,
            @Parameter(description = "데이터의 시작점", example = "0") @RequestParam(name = "offset") int offset,  // 데이터의 시작점
            @Parameter(description = "한 번에 가져올 데이터 크기", example = "10") @RequestParam(name = "limit") int limit) {  // 한 번에 가져올 데이터 크기

        log.info("CustmUserDetails : " + customUserDetails.getUsername());

        Page<SearchResultResponse> searchResults = searchService.search(query, offset, limit);

        if (searchResults.isEmpty()) {
            // 검색 결과가 없을 때도 200 상태 코드와 빈 리스트 반환
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(searchResults.getContent());
    }

    @GetMapping("/{type}/{placeId}")
    public ResponseEntity<PlaceDetailResponse> getSearchDetail(
            @Parameter(description = "장소 ID", example = "1") @PathVariable("placeId") Long id,
            @Parameter(description = "장소 유형(맛집 또는 관광)", example = "restaurant or tourist") @PathVariable("type") String type) {

        PlaceDetailResponse searchDetail;

        // 'type'에 따라 맛집 또는 관광지 조회
        if ("restaurant".equalsIgnoreCase(type)) {
            searchDetail = searchService.getSearchRestaurantDetail(id);
        } else if ("tourist".equalsIgnoreCase(type)) {
            searchDetail = searchService.getSearchTouristDetail(id);
        } else {
            throw new IllegalArgumentException("유효하지 않은 유형입니다.");
        }

        return ResponseEntity.ok(searchDetail);
    }
}


