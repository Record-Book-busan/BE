package busim.kkilogbu.record.service;

import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.record.dto.*;
import busim.kkilogbu.record.entity.Records;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import busim.kkilogbu.addressInfo.entity.AddressInfo;
import busim.kkilogbu.addressInfo.repository.AddressInfoRepository;
import busim.kkilogbu.contents.entity.Contents;
import busim.kkilogbu.global.redis.RedisService;
import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.user.entity.BlackList;
import busim.kkilogbu.user.entity.User;
import busim.kkilogbu.user.repository.BlackListRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {
	
	private final RecordRepository recordRepository;
	private final AddressInfoRepository addressInfoRepository;
	private final RedisService redisService;
	private final BlackListRepository blackListRepository;

	@Transactional(readOnly = true)
	public RecordDetailResponse getPlaceDetail(Long id) {
		Records records = recordRepository.findFetchById(id).orElseThrow(() ->
				new BaseException("해당하는 장소가 없습니다.", HttpStatus.NOT_FOUND)
		);
		return RecordMapper.toCreateRecordDetailResponse(records);
	}

	@Transactional
	public void createRecord(CreateRecordRequest request) {
		try {
			// Contents 객체 생성
			Contents contents = Contents.builder()
					.content(request.getContent())
					.title(request.getTitle())
					.imageUrl(request.getImageUrl())
					.build();

			// 해당 위치 정보가 있으면 가져오고 없으면 새로 생성
			AddressInfo addressInfo = addressInfoRepository.findByLatitudeAndLongitude(request.getLat(), request.getLng())
					.orElseGet(() -> AddressInfo.builder()
							.latitude(request.getLat())
							.longitude(request.getLng())
							.build());

			// Record 객체 생성 및 저장
			Records record = Records.createRecord(addressInfo, contents);
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


	@Transactional
	public void updateRecord(Long markId, UpdateRecordRequest request) {
		Records records = recordRepository.findFetchById(markId).orElseThrow(() -> {
			// TODO : custom exception 추가
			return new RuntimeException("해당하는 장소가 없습니다.");
		});
		Contents contents = records.getContents();
		AddressInfo oldAddress = records.getAddressInfo();
		double lat = oldAddress.getLatitude();
		double lng = oldAddress.getLongitude();

		// 위도 경도가 변경되었을 경우
		if (lat != request.getLat() || lng != request.getLng()) {
			AddressInfo addressInfo = addressInfoRepository.findByLatitudeAndLongitude(request.getLat(),
					request.getLng())
				.orElseGet(() -> AddressInfo.builder()
					.latitude(request.getLat())
					.longitude(request.getLng())
					.build());
			records.connect(addressInfo);

			oldAddress.getRecords().remove(records);
			// 기존 addressInfo에 record와 place가 없을 경우 삭제
			if (oldAddress.getRecords().isEmpty() && oldAddress.getPlace().isEmpty()) {
				addressInfoRepository.delete(oldAddress);
			}
		}
		contents.update(request.getContent(), request.getTitle(), request.getImageUrl());
	}

	@Transactional
	public void deleteRecord(Long markId) {
		Records records = recordRepository.findFetchById(markId).orElseThrow(() ->{
			// TODO : custom exception 추가
			return new RuntimeException("해당하는 장소가 없습니다.");
		});
		AddressInfo oldAddress = records.getAddressInfo();
		oldAddress.getRecords().remove(records);
		if (oldAddress.getRecords().isEmpty() && oldAddress.getPlace().isEmpty()) {
			addressInfoRepository.delete(oldAddress);
		}
		recordRepository.delete(records);
	}

	private RecordMarkResponse createRecordMarkResponse(Records records) {
		return RecordMarkResponse.builder()
			.id(records.getId())
			.lat(records.getAddressInfo().getLatitude())
			.lng(records.getAddressInfo().getLongitude())
			.imageUrl(records.getContents().getImageUrl())
			.build();
	}

	@Transactional
	public void report(Long markId) {
		// TODO : 로그인 구현후 수정
		User user = User.builder().nickname("tester").build();
		Records records = recordRepository.findById(markId)
			.orElseThrow(() -> new BaseException("해당하는 기록이 없습니다.", HttpStatus.NOT_FOUND));
		User writer = records.getUser();
		BlackList blocked = BlackList.builder().user(user).reportedUser(writer).build();
		blocked.report(user, writer);
		blackListRepository.save(blocked);
	}
}
