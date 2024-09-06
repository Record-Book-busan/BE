package busim.kkilogbu.record.controller;

import busim.kkilogbu.bookmark.service.BookmarkService;
import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.global.Ex.ErrorResponse;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kkilogbu/record")
@RequiredArgsConstructor
@Tag(name = "<RecordController>", description = "기록 관리 API")
public class RecordController {
	private final RecordService service;
	private final RedisService redisService;
	private final BookmarkService bookmarkService;

	@Operation(
			summary = "줌 레벨에 따라 기록을 조회합니다.",
			description = "사용자가 지정한 줌 레벨에 따라 기록을 조회합니다. 줌 레벨이 낮을수록 더 넓은 범위에서 데이터를 조회하고, 줌 레벨이 높을수록 좁은 범위에서 데이터를 조회합니다. 낮은 줌 레벨에서는 더 많은 기록이 클러스터링되어 표시되며, 높은 줌 레벨에서는 개별 기록이 상세하게 표시됩니다."
	)
	@GetMapping
	public ResponseEntity<List<RecordMarkResponse>> getRecordInRedisWithCluster(
			@Parameter(description = "위도") @RequestParam("lat") double lat,
			@Parameter(description = "경도") @RequestParam("lng") double lng,
			@Parameter(description = "줌 레벨", example = "LEVEL_1(0.1),\n" +
					"\tLEVEL_2(0.2),\n" +
					"\tLEVEL_3(0.3),\n" +
					"\tLEVEL_4(0.5),\n" +
					"\tLEVEL_5(1),\n" +
					"\tLEVEL_6(2.5),\n" +
					"\tLEVEL_7(5),\n" +
					"\tLEVEL_8(10),\n" +
					"\tLEVEL_9(20),\n" +
					"\tLEVEL_10(40),\n" +
					"\tLEVEL_11(80),\n" +
					"\tLEVEL_12(160),\n" +
					"\tLEVEL_13(320),\n" +
					"\tLEVEL_14(640),\n" +
					"\tLEVEL_15(1280)") @RequestParam("level") ZoomLevel level) {

			if (level == null) {
				throw new BaseException("줌 레벨이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
			}
		List<RecordMarkResponse> record = redisService.getPlacesFromRedis(lat, lng, level, "record",
			RecordMarkResponse.class);
			return ResponseEntity.ok(record);

	}




	@Operation(summary = "기록 상세 정보 가져오기", description = "특정 기록의 상세 정보를 가져옵니다.")
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
