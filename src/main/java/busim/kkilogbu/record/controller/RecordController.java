package busim.kkilogbu.record.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import busim.kkilogbu.bookmark.service.BookmarkService;
import busim.kkilogbu.global.redis.RedisService;
import busim.kkilogbu.record.dto.CreateRecordRequest;
import busim.kkilogbu.record.dto.RecordDetailResponse;
import busim.kkilogbu.record.dto.UpdateRecordRequest;
import busim.kkilogbu.record.service.RecordService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {
	private final RecordService service;
	private final RedisService redisService;
	private final BookmarkService bookmarkService;

	// @GetMapping
	// public ResponseEntity<List<Record>> getRecord(@PathParam("lat1") double lat1, @PathParam("lng1") double lng1, @PathParam("lat2") double lat2, @PathParam("lng2") double lng2){
	// 	return ResponseEntity.ok(service.getRecords(lat1, lng1, lat2, lng2));
	// }

	@GetMapping
	public ResponseEntity<List<Object>> getRecordInRedis(@PathParam("lat") double lat, @PathParam("lng") double lng,
		@PathParam("radius") double radius, @PathParam("category") Long category) {
		return ResponseEntity.ok(redisService.getPlacesInRedis(lat, lng, radius, "record", category));
	}

	@GetMapping("/{markId}")
	public ResponseEntity<RecordDetailResponse> getRecordDetail(@PathVariable Long markId){
		return ResponseEntity.ok(service.getPlaceDetail(markId));
	}

	@PostMapping
	public ResponseEntity<?> createRecord(@RequestBody @Valid CreateRecordRequest request) {
		service.createRecord(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{markId}/bookmark")
	public ResponseEntity<?> bookmark(@PathVariable Long markId) {
		bookmarkService.bookmark(markId, "record");
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{markId}/bookmark")
	public ResponseEntity<?> deleteBookmark(@PathVariable Long markId) {
		bookmarkService.unbookmark(markId, "record");
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{markId}")
	public ResponseEntity<?> updatePlace(@PathVariable Long markId, @RequestBody @Valid UpdateRecordRequest request) {
		service.updateRecord(markId, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{markId}")
	public ResponseEntity<?> deletePlace(@PathVariable Long markId) {
		service.deleteRecord(markId);
		return ResponseEntity.ok().build();
	}
}
