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
import busim.kkilogbu.global.redis.RedisService;
import busim.kkilogbu.place.dto.PlaceDetailResponse;
import busim.kkilogbu.place.entity.Place;
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
	@GetMapping
	public ResponseEntity<List<Object>> getPlace(@PathParam("lat") double lat, @PathParam("lng") double lng,
		@PathParam("radius") double radius, @PathParam("category") Long category) {
		return ResponseEntity.ok(redisService.getPlacesInRedis(lat, lng, radius, "place", category));
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
