package busim.kkilogbu.api.tourInfoAPI.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import busim.kkilogbu.place.dto.PlaceDetailResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourInfoService {
	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;
	private final Random random = new Random();
	@Value("${data.tourInfo.serviceKey}")
	private String SERVICE_KEY;
	private static final int NUM_OF_ROWS = 5;
	private static final String KEYWORD = "부산";
	private static final String BASE_URL = "https://apis.data.go.kr/B551011/KorService1/searchKeyword1";
	private static final String ARRANGE = "R";


	public List<PlaceDetailResponse> fetchTourInfoDate(){
		int page = random.nextInt(130) + 1;
		String apiUrl = buildUrl(page);
		String responseString = restTemplate.getForObject(apiUrl, String.class);
		List<PlaceDetailResponse> responses = new ArrayList<>();
		try {
			JsonNode root = objectMapper.readTree(responseString);
			JsonNode items = root.path("response").path("body").path("items").path("item");
			for (JsonNode item : items) {
				PlaceDetailResponse build = PlaceDetailResponse.builder()
					.title(item.path("title").asText())
					.address(item.path("addr1").asText())
					.addressDetail(item.path("addr2").asText())
					.lat(item.path("mapy").asDouble())
					.lng(item.path("mapx").asDouble())
					.imageUrl(item.path("firstimage").asText())
					.build();
				responses.add(build);
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		return responses;
	}

	private String buildUrl(int pageNo) {
		return BASE_URL + "?" + "serviceKey=" + SERVICE_KEY + "&numOfRows=" + NUM_OF_ROWS + "&pageNo=" + pageNo
			+ "&MobileOS=ETC&MobileApp=AppTest&_type=json&keyword=" + KEYWORD + "&arrange=" + ARRANGE;
	}

}
