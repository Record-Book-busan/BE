package busim.kkilogbu.place.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class PlaceController {
	private final PlaceService placeService;
	private final RedisService redisService;
	private final BookmarkService bookmarkService;

	/**
	 * 장소 목록 조회
	 */
	// TODO : front 랑 논의후 cluster를 front 에서 처리하면 해당 코드 삭제
	@GetMapping("/cluster")
	public ResponseEntity<List<Cluster>> getPlaceInRedisWithCluster(@PathParam("lat") double lat, @PathParam("lng") double lng,
		@PathParam("level") ZoomLevel level, @PathParam("category") Long category) {
		return ResponseEntity.ok(redisService.getPlacesInRedis(lat, lng, level, "place", category));
	}

	@GetMapping
	public ResponseEntity<List<PlaceMarkResponse>> getPlaceInRedis(@PathParam("lat") double lat, @PathParam("lng") double lng,
		@PathParam("level") ZoomLevel level, @PathParam("category") Long category) {
		return ResponseEntity.ok(redisService.getPlacesInRedis(lat, lng, level, PlaceMarkResponse.class, category));
	}
	/**
	 * 장소 상세 조회
	 *
	 */
	@GetMapping("/{placeId}")
	public ResponseEntity<PlaceDetailResponse> getPlaceDetail(@PathVariable("placeId") Long placeId) {
		return ResponseEntity.ok(placeService.getPlaceDetail(placeId));
	}

	@PostMapping("/{placeId}/bookmark")
	public ResponseEntity<?> bookmark(@PathVariable("placeId") Long placeId) {
		bookmarkService.bookmark(placeId, "place");
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{placeId}/bookmark")
	public ResponseEntity<?> deleteBookmark(@PathVariable("placeID") Long placeId) {
		bookmarkService.unbookmark(placeId, "place");
		return ResponseEntity.ok().build();
	}
}
