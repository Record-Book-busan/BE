package busim.kkilogbu.record.controller;

import busim.kkilogbu.api.externalTouristAPI.service.ExternalTouristService;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristIdImageResponse;
import busim.kkilogbu.bookmark.service.BookmarkService;
import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.global.ZoomLevel;
import busim.kkilogbu.global.redis.RedisService;
import busim.kkilogbu.record.dto.CreateRecordRequest;
import busim.kkilogbu.record.dto.RecordDetailResponse;
import busim.kkilogbu.record.dto.RecordMarkResponse;
import busim.kkilogbu.record.dto.UpdateRecordRequest;
import busim.kkilogbu.record.service.RecordService;
import busim.kkilogbu.user.service.BlackListService;
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
	private final RecordService recordService;
	private final RedisService redisService;
	private final BookmarkService bookmarkService;
	private final BlackListService blackListService;

	@Operation(
			summary = "줌 레벨에 따라 기록을 조회합니다.",
			description = "사용자가 지정한 줌 레벨에 따라 기록을 조회합니다. 줌 레벨이 낮을수록 더 넓은 범위에서 데이터를 조회하고, 줌 레벨이 높을수록 좁은 범위에서 데이터를 조회합니다."
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


		List<RecordMarkResponse> record = redisService.getPlacesFromRedis(lat, lng, level, "geo:record",
			RecordMarkResponse.class);
			return ResponseEntity.ok(record);

	}

	@GetMapping("/images")
	public ResponseEntity<List<TouristIdImageResponse>> fetchTouristImages(
			// @Parameter(description = "검색어", example = "서울") @RequestParam String query,
			@Parameter(description = "데이터의 시작점", example = "0") @RequestParam(defaultValue = "0") int offset,
			@Parameter(description = "한 번에 가져올 데이터 수", example = "9") @RequestParam(defaultValue = "6") int limit) {
		List<TouristIdImageResponse> imageUrls = recordService.getTouristGrid(offset, limit);
		return ResponseEntity.ok(imageUrls);
	}

	@GetMapping("/images/{touristId}")
	public ResponseEntity<RecordDetailResponse> getTouristDetail(@PathVariable Long touristId) {
		RecordDetailResponse recordDetail = recordService.getTouristDetail(touristId);
		return ResponseEntity.ok(recordDetail);
	}

	@Operation(summary = "기록 상세 정보 가져오기", description = "특정 기록의 상세 정보를 가져옵니다.")
	@GetMapping("/{markId}")
	public ResponseEntity<RecordDetailResponse> getRecordDetail(
			@Parameter(description = "기록 ID") @PathVariable Long markId) {
		return ResponseEntity.ok(recordService.getPlaceDetail(markId));
	}

	@Operation(summary = "새 기록 생성", description = "새로운 기록을 생성합니다.")
	@PostMapping("/auth")
	public ResponseEntity<?> createRecord(@RequestBody @Valid CreateRecordRequest request) {
		recordService.createRecord(request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "작성자 차단", description = "특정 기록의 작성자를 신고, 차단합니다.")
	@PostMapping("/auth/{markId}/report")
	public ResponseEntity<?> report(
			@Parameter(description = "기록 ID") @PathVariable Long markId) {
		blackListService.report(markId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "기록 북마크", description = "특정 기록을 북마크합니다.")
	@PostMapping("/auth/{markId}/bookmark")
	public ResponseEntity<?> bookmark(
			@Parameter(description = "기록 ID") @PathVariable Long markId) {
		bookmarkService.bookmark(markId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "북마크 삭제", description = "특정 기록의 북마크를 삭제합니다.")
	@DeleteMapping("/auth/{markId}/bookmark	")
	public ResponseEntity<?> deleteBookmark(
			@Parameter(description = "기록 ID") @PathVariable Long markId) {
		bookmarkService.unbookmark(markId, "record");
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "기록 수정", description = "기존 기록을 수정합니다.")
	@PatchMapping("/auth/{markId}")
	public ResponseEntity<?> updatePlace(
			@Parameter(description = "기록 ID") @PathVariable Long markId,
			@RequestBody @Valid UpdateRecordRequest request) {
		recordService.updateRecord(markId, request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "기록 삭제", description = "기존 기록을 삭제합니다.")
	@DeleteMapping("/auth/{markId}")
	public ResponseEntity<?> deletePlace(
			@Parameter(description = "기록 ID") @PathVariable Long markId) {
		recordService.deleteRecord(markId);
		return ResponseEntity.ok().build();
	}
}
