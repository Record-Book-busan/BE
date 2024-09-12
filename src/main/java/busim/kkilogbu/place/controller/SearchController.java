package busim.kkilogbu.place.controller;

import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.place.dto.SearchResultResponse;
import busim.kkilogbu.place.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/kkilogbu/place")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "검색", description = "검색어를 기반으로 맛집 또는 관광지를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공"),
            @ApiResponse(responseCode = "200", description = "검색 결과가 없음")
    })
    @GetMapping("/search")
    public ResponseEntity<List<SearchResultResponse>> search(
            @Parameter(description = "검색어", example = "맛집") @RequestParam(name = "query") String query,
            @Parameter(description = "데이터의 시작점", example = "0") @RequestParam(name = "offset") int offset,  // 데이터의 시작점
            @Parameter(description = "한 번에 가져올 데이터 크기", example = "10") @RequestParam(name = "limit") int limit) {  // 한 번에 가져올 데이터 크기

        Page<SearchResultResponse> searchResults = searchService.search(query, offset, limit);

        if (searchResults.isEmpty()) {
            // 검색 결과가 없을 때도 200 상태 코드와 빈 리스트 반환
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(searchResults.getContent());
    }
}