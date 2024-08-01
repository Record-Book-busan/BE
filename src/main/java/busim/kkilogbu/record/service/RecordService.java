package busim.kkilogbu.record.service;

import static java.time.LocalDateTime.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import busim.kkilogbu.addressInfo.entity.AddressInfo;
import busim.kkilogbu.addressInfo.repository.AddressInfoRepository;
import busim.kkilogbu.contents.entity.Contents;
import busim.kkilogbu.global.redis.RedisService;
import busim.kkilogbu.record.dto.CreateRecordRequest;
import busim.kkilogbu.record.dto.RecordDetailResponse;
import busim.kkilogbu.record.dto.UpdateRecordRequest;
import busim.kkilogbu.record.entity.Record;
import busim.kkilogbu.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {
	private final RecordRepository recordRepository;
	private final AddressInfoRepository addressInfoRepository;
	private final RedisService redisService;

	// TODO : 주변 반경 설정이 있으면 좋을거 같기두 하고 아닌거 같기두 하고(1km당 0.01+-)
	public List<Record> getRecords(double lat1, double lng1, double lat2, double lng2){
		List<AddressInfo> points = addressInfoRepository.findByLatitudeBetweenAndLongitudeBetween(
			lat1, lng1, lat2, lng2, null);
		if(!points.isEmpty()) {
			return null;
		}
		return recordRepository.findByAddressInfoIn(points.stream().map(AddressInfo::getId).toList());
	}

	public RecordDetailResponse getPlaceDetail(Long id){
		Record record = recordRepository.findFetchById(id).orElseThrow(() -> {
			// TODO : custom exception 추가?
			return new RuntimeException("해당하는 장소가 없습니다.");
		});
		return RecordDetailResponse.builder()
			.id(record.getId())
			.cat1(record.getCat1())
			.cat2(record.getCat2())
			.createdAt(record.getCreatedAt())
			.address(record.getAddressInfo().getAddress())
			.addressDetail(record.getAddressInfo().getAddressDetail())
			.zipcode(record.getAddressInfo().getZipcode())
			.lat(record.getAddressInfo().getLatitude())
			.lng(record.getAddressInfo().getLongitude())
			.content(record.getContents().getContent())
			.imageUrl(record.getContents().getImageUrl())
			.title(record.getContents().getTitle())
			// TODO : 로그인 구현후 추가
			// .nickName(record.getUser().getNickName())
			.build();
	}

	@Transactional
	public void createRecord(CreateRecordRequest request) {
		Contents contents = Contents.builder()
			.content(request.getContent())
			.title(request.getTitle())
			.imageUrl(request.getImageUrl())
			.build();

		// 해당 위치 정보가 있으면 가져오고 없으면 생성
		AddressInfo addressInfo = addressInfoRepository.findByLatitudeAndLongitude(request.getLat(), request.getLng())
			.orElseGet(() -> AddressInfo.builder().address(request.getAddress())
				.addressDetail(request.getAddressDetail())
				.zipcode(request.getZipcode())
				.latitude(request.getLat())
				.longitude(request.getLng())
				.build());

		Record record = Record.builder()
			.cat1(request.getCat1())
			.cat2(request.getCat2())
			.createdAt(now())
			.build();
		// TODO : 로그인 기능 제작후 수정
		record.connect(null, addressInfo, contents);
		recordRepository.save(record);
		// TODO : 무한 참조 발생, DTO 생성후 수정
		// redisService.saveRecordsInRedis(request.getLat(), request.getLng(), record);
	}

	@Transactional
	public void updateRecord(Long markId, UpdateRecordRequest request) {
		Record record = recordRepository.findFetchById(markId).orElseThrow(() -> {
			// TODO : custom exception 추가
			return new RuntimeException("해당하는 장소가 없습니다.");
		});
		Contents contents = record.getContents();
		AddressInfo oldAddress = record.getAddressInfo();
		double lat = oldAddress.getLatitude();
		double lng = oldAddress.getLongitude();

		// 위도 경도가 변경되었을 경우
		if (lat != request.getLat() || lng != request.getLng()) {
			AddressInfo addressInfo = addressInfoRepository.findByLatitudeAndLongitude(request.getLat(),
					request.getLng())
				.orElseGet(() -> AddressInfo.builder().address(request.getAddress())
					.addressDetail(request.getAddressDetail())
					.zipcode(request.getZipcode())
					.latitude(request.getLat())
					.longitude(request.getLng())
					.build());
			record.connect(addressInfo);

			oldAddress.getRecord().remove(record);
			// 기존 addressInfo에 record와 place가 없을 경우 삭제
			if (oldAddress.getRecord().isEmpty() && oldAddress.getPlace().isEmpty()) {
				addressInfoRepository.delete(oldAddress);
			}
		}
		contents.update(request.getContent(), request.getTitle(), request.getImageUrl());
		record.update(request.getCat1(), request.getCat2());
	}

	@Transactional
	public void deleteRecord(Long markId) {
		Record record = recordRepository.findFetchById(markId).orElseThrow(() ->{
			// TODO : custom exception 추가
			return new RuntimeException("해당하는 장소가 없습니다.");
		});
		AddressInfo oldAddress = record.getAddressInfo();
		oldAddress.getRecord().remove(record);
		if (oldAddress.getRecord().isEmpty() && oldAddress.getPlace().isEmpty()) {
			addressInfoRepository.delete(oldAddress);
		}
		recordRepository.delete(record);
	}
}
