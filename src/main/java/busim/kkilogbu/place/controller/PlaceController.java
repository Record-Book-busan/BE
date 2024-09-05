package busim.kkilogbu.place.controller;


import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.global.Ex.InvalidCategoryException;
import busim.kkilogbu.global.redis.dto.Cluster;
import busim.kkilogbu.place.dto.RestaurantCategory;
import busim.kkilogbu.place.dto.TouristCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import busim.kkilogbu.bookmark.service.BookmarkService;
import busim.kkilogbu.global.ZoomLevel;
import busim.kkilogbu.global.redis.RedisService;

import busim.kkilogbu.place.dto.PlaceMarkResponse;
import busim.kkilogbu.place.service.PlaceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kkilogbu/place")
@Tag(name = "<PlaceController>", description = "메인페이지에 있는 카테고리")
public class PlaceController {

	private final PlaceService placeService;
	private final RedisService redisService;
	private final BookmarkService bookmarkService;

	@Operation(summary = "카테고리 조회", description = "맛집, 관광 정보 가져옵니다")
	@GetMapping
	public ResponseEntity<List<Cluster<PlaceMarkResponse>>> getPlaceInRedis(
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
					"\tLEVEL_15(1280)") @RequestParam("level") ZoomLevel level,
			@Parameter(description = "맛집 카테고리들, 예: NORMAL_RESTAURANT(일반 맛집), SPECIAL_RESTAURANT(특별 맛집)")
			@RequestParam(value = "restaurantCategories", required = false) List<RestaurantCategory> restaurantCategories,
			@Parameter(description = "관광 카테고리들, 예: TOURIST_SPOT(관광지), THEME(테마), HOT_PLACE(핫플), NATURE(자연), LEISURE_SPORTS(레포츠)")
			@RequestParam(value = "touristCategories", required = false) List<TouristCategory> touristCategories) {

		// 빈 리스트나 null 여부 확인 후 처리
		boolean hasRestaurantCategories = restaurantCategories != null && !restaurantCategories.isEmpty();
		boolean hasTouristCategories = touristCategories != null && !touristCategories.isEmpty();

		// 둘 다 null이거나 빈 리스트인 경우 예외 처리
		if (!hasRestaurantCategories && !hasTouristCategories) {
			throw new InvalidCategoryException("맛집 카테고리 또는 관광 카테고리 중 하나는 반드시 선택해야 합니다.");
		}

		try {
			List<Cluster<PlaceMarkResponse>> places;

			// 관광 카테고리만 있는 경우
			if (!hasRestaurantCategories && hasTouristCategories) {
				places = redisService.getTouristPlacesNew(lat, lng, level, touristCategories);
			}
			// 맛집 카테고리만 있는 경우
			else if (hasRestaurantCategories && !hasTouristCategories) {
				places = redisService.getRestaurantPlacesNew(lat, lng, level, restaurantCategories);
			}
			// 둘 다 있는 경우
			else {
				places = redisService.getAllPlacesNew(lat, lng, level, restaurantCategories, touristCategories);
			}

			return ResponseEntity.ok(places);

		} catch (IllegalArgumentException e) {
			throw new InvalidCategoryException("유효하지 않은 카테고리 값이 포함되어 있습니다: " + e.getMessage());
		} catch (Exception e) {
			throw new BaseException("서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}






//	/**
//	 * 장소 상세 조회
//	 *
//	 */
//	@Operation(summary = "카테고리 상세 조회", description = "카테고리에서 선택해서 나온 마커 중 한 곳을 선택 했을 때, 장소의 상세 정보를 가져옵니다.")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "성공적으로 장소 정보를 가져왔습니다."),
//			@ApiResponse(responseCode = "404", description = "장소를 찾을 수 없습니다.")})
//			@GetMapping("/{placeId}")
//			public ResponseEntity<PlaceDetailResponse> getPlaceDetail(
//			@Parameter(description = "장소 ID", example = "1") @PathVariable("placeId") Long placeId,
//			@Parameter(description = "장소 유형(맛집 또는 관광)", example = "restaurant or tourist") @RequestParam("type") String type) {
//
//		// 'type'에 따라 맛집 또는 관광지 조회
//		PlaceDetailResponse placeDetail;
//		if ("restaurant".equalsIgnoreCase(type)) {
//			placeDetail = placeService.getRestaurantDetail(placeId);
//		} else if ("tourist".equalsIgnoreCase(type)) {
//			placeDetail = placeService.getTouristDetail(placeId);
//		} else {
//			throw new IllegalArgumentException("유효하지 않은 유형입니다.");
//		}
//
//		return ResponseEntity.ok(placeDetail);
//	}
//


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
