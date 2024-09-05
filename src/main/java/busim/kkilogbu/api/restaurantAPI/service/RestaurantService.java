package busim.kkilogbu.api.restaurantAPI.service;

import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;
import busim.kkilogbu.api.restaurantAPI.repository.RestaurantRepository;
import busim.kkilogbu.place.dto.RestaurantCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;

	@Transactional
	public void saveCSVData(MultipartFile file) {
		log.info("CSV 파일 처리 시작 - 파일 이름: {}", file.getOriginalFilename());  // 파일 이름도 로그에 추가

		try (BufferedReader fileReader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
			 CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

			List<Restaurant> restaurantList = new ArrayList<>();
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			log.info("CSV 레코드 수: {}", ((List<?>) csvRecords).size());  // CSV 레코드 수를 로그로 출력

			for (CSVRecord csvRecord : csvRecords) {
				log.debug("처리 중인 레코드: {}", csvRecord);  // 각 레코드의 전체 데이터 로그로 출력

				// 레스토랑 객체 빌드 시작 전 파라미터 로그로 확인
				log.debug("제목: {}, 도로명주소: {}, 지번주소: {}, 위도: {}, 경도: {}, 대표전화: {}, 업태구분: {}, 상세내용: {}, 이미지: {}, 분류: {}, 유형: {}",
						csvRecord.get("제목"), csvRecord.get("도로명주소"), csvRecord.get("지번주소"),
						csvRecord.get("위도"), csvRecord.get("경도"), csvRecord.get("식당대표전화번호"),
						csvRecord.get("업태구분명"), csvRecord.get("상세내용"), csvRecord.get("이미지"),
						csvRecord.get("분류"), csvRecord.get("유형"));

				Restaurant restaurant = Restaurant.builder()
						.title(csvRecord.get("제목"))
						.address(csvRecord.get("도로명주소"))  // 주소를 도로명주소로 수정
						.landLotAddress(csvRecord.get("지번주소"))  // 지번주소는 별도 필드로 처리
						.latitude(parseDouble(csvRecord.get("위도")))
						.longitude(parseDouble(csvRecord.get("경도")))
						.phoneNumber(csvRecord.get("식당대표전화번호"))
						.businessType(csvRecord.get("업태구분명"))
						.detailedInformation(csvRecord.get("상세내용"))
						.imageUrls(parseImageUrls(csvRecord.get("이미지")))  // 쉼표로 구분된 이미지 처리
						.categories(parseCategories(csvRecord.get("분류")))  // 쉼표로 구분된 카테고리 처리
						.type(RestaurantCategory.fromType(parseInteger(csvRecord.get("유형"))))  // 유형 처리
						.build();

				restaurantList.add(restaurant);
				log.info("레스토랑 객체 빌드 완료: {}", restaurant);  // 각 레코드 처리 후 로그 찍기
			}

			log.info("총 {}개의 레스토랑 데이터를 저장합니다.", restaurantList.size());  // 저장할 데이터 개수 출력
			restaurantRepository.saveAll(restaurantList);
			log.info("CSV 데이터 저장 완료");  // 데이터 저장 완료 후 로그 찍기

		} catch (IOException e) {
			log.error("CSV 파일을 처리하는 동안 오류가 발생했습니다: {}", e.getMessage());  // 오류 발생 시 로그 찍기
			throw new RuntimeException("CSV 파일을 처리하는 동안 오류가 발생했습니다: " + e.getMessage());
		}
	}

	// 이미지를 쉼표(',')로 구분된 문자열로 처리하는 메소드
	private List<String> parseImageUrls(String imageUrlString) {
		log.debug("이미지 URL 문자열 파싱 시작: {}", imageUrlString);  // 이미지 URL 파싱 시작 로그
		if (imageUrlString == null || imageUrlString.trim().isEmpty()) {
			log.debug("이미지 URL이 비어있음");
			return new ArrayList<>();
		}
		String[] imageUrls = imageUrlString.split(",");
		List<String> result = new ArrayList<>();
		for (String url : imageUrls) {
			result.add(url.trim());
		}
		log.debug("이미지 URL 파싱 결과: {}", result);  // 이미지 URL 파싱 결과 로그 출력
		return result;
	}

	// CSV의 '분류' 필드를 쉼표(',')로 구분하여 List<String>으로 처리하는 메소드
	private List<String> parseCategories(String categoryString) {
		log.debug("카테고리 문자열 파싱 시작: {}", categoryString);  // 카테고리 파싱 시작 로그
		if (categoryString == null || categoryString.trim().isEmpty()) {
			log.debug("카테고리 필드가 비어있음");
			return new ArrayList<>();
		}
		String[] categories = categoryString.split(",");
		List<String> result = new ArrayList<>();
		for (String category : categories) {
			result.add(category.trim());
		}
		log.debug("카테고리 파싱 결과: {}", result);  // 카테고리 파싱 결과 로그 출력
		return result;
	}

	// '유형' 필드를 Integer로 변환하는 메소드
	private Integer parseInteger(String value) {
		log.debug("유형 문자열 파싱 시작: {}", value);  // 유형 파싱 시작 로그
		try {
			Integer result = value != null ? Integer.parseInt(value) : null;
			log.debug("유형 파싱 결과: {}", result);  // 유형 파싱 결과 로그 출력
			return result;
		} catch (NumberFormatException e) {
			log.error("유형 파싱 중 오류 발생: {} (잘못된 값)", value);  // 오류 발생 시 로그 찍기
			return null;
		}
	}

	// 문자열을 Double로 변환하는 메소드
	private Double parseDouble(String value) {
		log.debug("위도/경도 문자열 파싱 시작: {}", value);  // 위도/경도 파싱 시작 로그
		try {
			Double result = value != null && !value.trim().isEmpty() ? Double.parseDouble(value.trim()) : null;
			log.debug("파싱된 위도/경도 값: {} -> {}", value, result);  // 위도/경도 파싱 결과 로그 출력
			return result;
		} catch (NumberFormatException e) {
			log.error("위도/경도 파싱 중 오류 발생: {} (잘못된 값)", value);  // 오류 발생 시 로그 찍기
			return null;
		}
	}
}
