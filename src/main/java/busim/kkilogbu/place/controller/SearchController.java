package busim.kkilogbu.place.controller;


import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.place.dto.SearchResultResponse;
import busim.kkilogbu.place.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kkilogbu/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    public ResponseEntity<List<SearchResultResponse>> search(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "offset") int offset,  // 데이터의 시작점
            @RequestParam(name = "limit") int limit) {  // 한 번에 가져올 데이터 크기

        Page<SearchResultResponse> searchResults = searchService.search(query, offset, limit);

        if (searchResults.isEmpty()) {
            throw new BaseException("검색 결과가 없습니다.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(searchResults.getContent());
    }
}
