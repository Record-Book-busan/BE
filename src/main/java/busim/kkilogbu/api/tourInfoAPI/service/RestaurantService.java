package busim.kkilogbu.api.tourInfoAPI.service;

import busim.kkilogbu.api.tourInfoAPI.domain.entity.Restaurant;
import busim.kkilogbu.api.tourInfoAPI.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;

	@Transactional
	public void saveCSVData(MultipartFile file) {
		try (BufferedReader fileReader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
			 CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

			List<Restaurant> restaurantList = new ArrayList<>();
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				Restaurant restaurant = Restaurant.builder()
						.title(csvRecord.get("제목"))
						.address(csvRecord.get("주소"))
						.landLotAddress(csvRecord.get("지번주소"))
						.latitude(parseDouble(csvRecord.get("위도")))
						.longitude(parseDouble(csvRecord.get("경도")))
						.phoneNumber(csvRecord.get("식당대표전화번호"))
						.businessType(csvRecord.get("업태구분명"))
						.businessLicenseName(csvRecord.get("영업인허가명"))
						.detailedInformation(csvRecord.get("상세내용"))
						.imageUrls(parseImageUrls(csvRecord.get("이미지")))
						.restaurantName(csvRecord.get("식당명"))
						.category(csvRecord.get("분류"))
						.build();

				restaurantList.add(restaurant);
			}

			restaurantRepository.saveAll(restaurantList);
		} catch (IOException e) {
			throw new RuntimeException("CSV 파일을 처리하는 동안 오류가 발생했습니다: " + e.getMessage());
		}
	}

	private List<String> parseImageUrls(String imageUrlString) {
		if (imageUrlString == null || imageUrlString.trim().isEmpty()) {
			return new ArrayList<>();
		}
		// TODO : 이미지가 '|'로 구분된다고 가정
		String[] imageUrls = imageUrlString.split("\\|");
		List<String> result = new ArrayList<>();
		for (String url : imageUrls) {
			result.add(url.trim());
		}
		return result;
	}

	private Double parseDouble(String value) {
		try {
			return value != null ? Double.parseDouble(value) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
}