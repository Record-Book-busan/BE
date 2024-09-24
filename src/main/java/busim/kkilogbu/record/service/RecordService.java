package busim.kkilogbu.record.service;

import busim.kkilogbu.addressInfo.entity.AddressInfo;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristGridResponse;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristIdImageResponse;
import busim.kkilogbu.api.touristAPI.domain.dto.TouristMapper;
import busim.kkilogbu.api.touristAPI.domain.entity.Tourist;
import busim.kkilogbu.api.touristAPI.repository.TouristRepository;
import busim.kkilogbu.contents.entity.Contents;
import busim.kkilogbu.global.Ex.BaseException;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import busim.kkilogbu.addressInfo.repository.AddressInfoRepository;
import busim.kkilogbu.global.redis.RedisService;
import busim.kkilogbu.record.dto.CreateRecordRequest;
import busim.kkilogbu.record.dto.RecordDetailResponse;
import busim.kkilogbu.record.dto.RecordMapper;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.security.service.CustomUserDetailsService;
import busim.kkilogbu.user.entity.users.Users;
import busim.kkilogbu.user.service.BlackListService;
import busim.kkilogbu.user.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {
	
	private final RecordRepository recordRepository;
	private final AddressInfoRepository addressInfoRepository;
	private final RedisService redisService;
	private final BlackListService blackListService;
	private final TouristRepository touristRepository;
	private final UserService userService;


	@Transactional(readOnly = true)
	public List<TouristIdImageResponse> getTouristGrid(String query, int offset, int limit) {
		int page = offset / limit;  // offset을 페이지 번호로 변환

		Pageable pageable = PageRequest.of(page, limit); // 페이지와 사이즈 설정
		Page<Tourist> tourists = touristRepository.findByMultipleFields(query, pageable);

		// Tourist 객체에서 id와 imageUrl만 추출하여 TouristIdImageResponse로 변환
		return tourists.stream()
				.map(tourist -> new TouristIdImageResponse(tourist.getId(), tourist.getImageUrl()))  // id와 imageUrl 추출
				.collect(Collectors.toList());
	}


	@Transactional(readOnly = true)
	public TouristGridResponse getTouristDetail(Long touristId) {
		// touristId로 관광지 조회
		Tourist tourist = touristRepository.findById(touristId)
				.orElseThrow(() -> new BaseException("관광지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

		// Tourist 엔티티를 TouristGridResponse로 변환하여 반환
		return TouristMapper.toGridResponse(tourist);
	}

	@Transactional(readOnly = true)
	public RecordDetailResponse getPlaceDetail(Long id) {
		Users user = userService.getCurrentUser();
		Records records = recordRepository.findFetchById(id).orElseThrow(() ->
				new BaseException("해당하는 장소가 없습니다.", HttpStatus.NOT_FOUND)
		);


		if(blackListService.isBlocked(user, records.getUsers())){
			throw new BaseException("차단된 사용자입니다.", HttpStatus.FORBIDDEN);
		}

		return RecordMapper.toCreateRecordDetailResponse(records);
	}

	@Transactional
	public void createRecord(CreateRecordRequest request) {
		try {
			Users currentUser = userService.getCurrentUser();

			// Contents 객체 생성
			Contents contents = Contents.builder()
					.content(request.getContent())
					.title(request.getTitle())
					.imageUrl(request.getImageUrl())
					.build();

			// 해당 위치 정보가 있으면 가져오고 없으면 새로 생성
			AddressInfo addressInfo = addressInfoRepository.findByLatitudeAndLongitude(request.getLat(), request.getLng())
					.orElseGet(() -> AddressInfo.builder()
							.address(request.getAddress())
							.latitude(request.getLat())
							.longitude(request.getLng())
							.build());


			// Record 객체 생성 및 저장
			Records record = Records.createRecord(currentUser, addressInfo, contents);
			recordRepository.save(record);

			// Redis에 저장
			redisService.saveTotalPlaceInRedis(
					request.getLat(),
					request.getLng(),
					RecordMapper.toCreateRecordDetailResponse(record),
					"record"
			);

		} catch (DataAccessException e) {
			throw new BaseException("데이터베이스 작업 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			throw new BaseException("기록 생성 중 예상치 못한 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


//	@Transactional
//	public void updateRecord(Long markId, UpdateRecordRequest request) {
//		Records records = recordRepository.findFetchById(markId).orElseThrow(() -> {
//			// TODO : custom exception 추가
//			return new RuntimeException("해당하는 장소가 없습니다.");
//		});
//		Contents contents = records.getContents();
//		AddressInfo oldAddress = records.getAddressInfo();
//		double lat = oldAddress.getLatitude();
//		double lng = oldAddress.getLongitude();
//
//		// 위도 경도가 변경되었을 경우
//		if (lat != request.getLat() || lng != request.getLng()) {
//			AddressInfo addressInfo = addressInfoRepository.findByLatitudeAndLongitude(request.getLat(),
//					request.getLng())
//				.orElseGet(() -> AddressInfo.builder()
//					.latitude(request.getLat())
//					.longitude(request.getLng())
//					.build());
//			records.connect(addressInfo);
//
//			oldAddress.getRecords().remove(records);
//			// 기존 addressInfo에 record와 place가 없을 경우 삭제
//			if (oldAddress.getRecords().isEmpty() && oldAddress.getPlace().isEmpty()) {
//				addressInfoRepository.delete(oldAddress);
//			}
//		}
//		contents.update(request.getContent(), request.getTitle(), request.getImageUrl());
//	}

	@Transactional
	public void deleteRecord(Long markId) {
		Users user = userService.getCurrentUser();
		Records records = recordRepository.findByUsersAndId(user, markId).orElseThrow(() ->{
			return new BaseException("해당하는 장소가 없습니다.", HttpStatus.NOT_FOUND);
		});
		AddressInfo oldAddress = records.getAddressInfo();
		oldAddress.getRecords().remove(records);
		if (oldAddress.getRecords().isEmpty() && oldAddress.getPlace().isEmpty()) {
			addressInfoRepository.delete(oldAddress);
		}
		recordRepository.delete(records);
	}

//	private RecordMarkResponse createRecordMarkResponse(Records records) {
//		return RecordMarkResponse.builder()
//			.id(records.getId())
//			.lat(records.getAddressInfo().getLatitude())
//			.lng(records.getAddressInfo().getLongitude())
//			.imageUrl(records.getContents().getImageUrl())
//			.build();
//	}
}
