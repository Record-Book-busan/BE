package busim.kkilogbu.record.controller;

import busim.kkilogbu.bookmark.service.BookmarkService;
import busim.kkilogbu.global.ZoomLevel;
import busim.kkilogbu.global.redis.RedisService;
import busim.kkilogbu.global.redis.dto.Cluster;
import busim.kkilogbu.record.dto.CreateRecordRequest;
import busim.kkilogbu.record.dto.RecordDetailResponse;
import busim.kkilogbu.record.dto.RecordMarkResponse;
import busim.kkilogbu.record.dto.UpdateRecordRequest;
import busim.kkilogbu.record.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
@Tag(name = "<RecordController>", description = "기록 관리 API")
public class RecordController {
	private final RecordService service;
	private final RedisService redisService;
	private final BookmarkService bookmarkService;

	@Operation(summary = "주변 기록 가져오기", description = "Redis에서 주변 기록을 가져옵니다.")
	@GetMapping("/cluster")
	public ResponseEntity<List<Cluster>> getRecordInRedisWithCluster(
			@Parameter(description = "위도") @PathParam("lat") double lat,
			@Parameter(description = "경도") @PathParam("lng") double lng,
			@Parameter(description = "줌 레벨") @PathParam("level") ZoomLevel level,
			@Parameter(description = "카테고리 ID") @PathParam("category") Long category) {
		return ResponseEntity.ok(redisService.getPlacesInRedis(lat, lng, level, "record", category));
	}

	@Operation(summary = "기록 가져오기", description = "Redis에서 기록을 가져옵니다.")
	@GetMapping
	public ResponseEntity<List<RecordMarkResponse>> getRecordInRedis(
			@Parameter(description = "위도") @PathParam("lat") double lat,
			@Parameter(description = "경도") @PathParam("lng") double lng,
			@Parameter(description = "줌 레벨") @PathParam("level") ZoomLevel level,
			@Parameter(description = "카테고리 ID") @PathParam("category") Long category) {
		return ResponseEntity.ok(redisService.getPlacesInRedis(lat, lng, level, RecordMarkResponse.class, category));
	}

	@Operation(summary = "기록 상세 정보 가져오기", description = "특정 기록의 상세 정보를 가져옵니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공적으로 기록을 가져옴", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "기록을 찾을 수 없음")
	})
	@GetMapping("/{markId}")
	public ResponseEntity<RecordDetailResponse> getRecordDetail(
			@Parameter(description = "기록 ID") @PathVariable Long markId) {
		return ResponseEntity.ok(service.getPlaceDetail(markId));
	}

	@Operation(summary = "새 기록 생성", description = "새로운 기록을 생성합니다.")
	@PostMapping
	public ResponseEntity<?> createRecord(@RequestBody @Valid CreateRecordRequest request) {
		service.createRecord(request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "기록 북마크", description = "특정 기록을 북마크합니다.")
	@PostMapping("/{markId}/bookmark")
	public ResponseEntity<?> bookmark(
			@Parameter(description = "기록 ID") @PathVariable Long markId) {
		bookmarkService.bookmark(markId, "record");
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "북마크 삭제", description = "특정 기록의 북마크를 삭제합니다.")

	@DeleteMapping("/{markId}/bookmark")
	public ResponseEntity<?> deleteBookmark(
			@Parameter(description = "기록 ID") @PathVariable Long markId) {
		bookmarkService.unbookmark(markId, "record");
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "기록 수정", description = "기존 기록을 수정합니다.")
	@PatchMapping("/{markId}")
	public ResponseEntity<?> updatePlace(
			@Parameter(description = "기록 ID") @PathVariable Long markId,
			@RequestBody @Valid UpdateRecordRequest request) {
		service.updateRecord(markId, request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "기록 삭제", description = "기존 기록을 삭제합니다.")
	@DeleteMapping("/{markId}")
	public ResponseEntity<?> deletePlace(
			@Parameter(description = "기록 ID") @PathVariable Long markId) {
		service.deleteRecord(markId);
		return ResponseEntity.ok().build();
	}
}
