package busim.kkilogbu.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import busim.kkilogbu.bookmark.service.BookmarkService;
import busim.kkilogbu.global.ZoomLevel;
import busim.kkilogbu.global.redis.RedisService;
import busim.kkilogbu.global.redis.dto.Cluster;
import busim.kkilogbu.place.dto.PlaceDetailResponse;
import busim.kkilogbu.place.dto.PlaceMarkResponse;
import busim.kkilogbu.place.service.PlaceService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place")
@Tag(name = "<PlaceController>", description = "장소 관련 API")
public class PlaceController {
	private final PlaceService placeService;
	private final RedisService redisService;
	private final BookmarkService bookmarkService;


	/**
	 * 장소 목록 조회
	 */
	@Operation(summary = "클러스터링된 장소 목록 조회", description = "Redis에서 클러스터링된 장소 목록을 가져옵니다.")
	@GetMapping("/cluster")
	public ResponseEntity<List<Cluster>> getPlaceInRedisWithCluster(
			@Parameter(description = "위도") @PathParam("lat") double lat,
			@Parameter(description = "경도") @PathParam("lng") double lng,
			@Parameter(description = "줌 레벨") @PathParam("level") ZoomLevel level,
			@Parameter(description = "카테고리 ID") @PathParam("category") Long category) {
		return ResponseEntity.ok(redisService.getPlacesInRedis(lat, lng, level, "place", category));
	}

	@Operation(summary = "장소 목록 조회", description = "Redis에서 장소 목록을 가져옵니다.")
	@GetMapping
	public ResponseEntity<List<PlaceMarkResponse>> getPlaceInRedis(
			@Parameter(description = "위도") @PathParam("lat") double lat,
			@Parameter(description = "경도") @PathParam("lng") double lng,
			@Parameter(description = "줌 레벨") @PathParam("level") ZoomLevel level,
			@Parameter(description = "카테고리 ID") @PathParam("category") Long category) {
		return ResponseEntity.ok(redisService.getPlacesInRedis(lat, lng, level, PlaceMarkResponse.class, category));
	}

	/**
	 * 장소 상세 조회
	 */
	@Operation(summary = "장소 상세 조회", description = "특정 장소의 상세 정보를 가져옵니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공적으로 장소 정보를 가져왔습니다."),
			@ApiResponse(responseCode = "404", description = "장소를 찾을 수 없습니다.")
	})
	@GetMapping("/{placeId}")
	public ResponseEntity<PlaceDetailResponse> getPlaceDetail(
			@Parameter(description = "장소 ID", example = "1") @PathVariable("placeId") Long placeId) {
		return ResponseEntity.ok(placeService.getPlaceDetail(placeId));
	}



	@Operation(summary = "장소 북마크 추가", description = "특정 장소를 북마크합니다.")
	@PostMapping("/{placeId}/bookmark")
	public ResponseEntity<?> bookmark(
			@Parameter(description = "장소 ID", example = "1") @PathVariable("placeId") Long placeId) {
		bookmarkService.bookmark(placeId, "place");
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "장소 북마크 삭제", description = "특정 장소의 북마크를 삭제합니다.")
	@DeleteMapping("/{placeId}/bookmark")
	public ResponseEntity<?> deleteBookmark(
			@Parameter(description = "장소 ID", example = "1") @PathVariable("placeId") Long placeId) {
		bookmarkService.unbookmark(placeId, "place");
		return ResponseEntity.ok().build();
	}
}
