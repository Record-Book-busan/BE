package busim.kkilogbu.place.service;

import org.springframework.stereotype.Service;

import busim.kkilogbu.place.dto.PlaceDetailResponse;
import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {
	private final PlaceRepository placeRepository;

	public PlaceDetailResponse getPlaceDetail(Long placeId) {
		Place place = placeRepository.findById(placeId)
			.orElseThrow(() -> new IllegalArgumentException("해당 장소가 존재하지 않습니다."));
		return PlaceDetailResponse.builder()
			.id(place.getId())
			.title(place.getContents().getTitle())
			.content(place.getContents().getContent())
			.imageUrl(place.getContents().getImageUrl())
			.address(place.getAddressInfo().getAddress())
			.addressDetail(place.getAddressInfo().getAddressDetail())
			.zipcode(place.getAddressInfo().getZipcode())
			.lat(place.getAddressInfo().getLatitude())
			.lng(place.getAddressInfo().getLongitude())
			.cat1(place.getCat1())
			.cat2(place.getCat2())
			.operatingTime(place.getOperatingTime())
			.phone(place.getPhone())
			.build();
	}
}
