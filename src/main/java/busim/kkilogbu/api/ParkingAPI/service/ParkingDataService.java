package busim.kkilogbu.api.ParkingAPI.service;

import busim.kkilogbu.api.ParkingAPI.domain.ParkingDataMapper;
import busim.kkilogbu.api.ParkingAPI.domain.dto.ParkingDataRequestDto;
import busim.kkilogbu.api.ParkingAPI.domain.entity.ParkingData;
import busim.kkilogbu.api.ParkingAPI.repository.ParkingRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingDataService {

    private final ParkingRepository repository;
    private final ObjectMapper objectMapper;

    private static final String SERVICE_KEY = "MkvGYtjqXwhLG79ljGRPqBYsjnMT9M6atTQI0YofGQJQaPIj4miDdSkxAPNQWlVLuN%2FbFkbX3CeAjNGP3JNlrA%3D%3D";
    private static final String BASE_URL = "http://apis.data.go.kr/6260000/BusanPblcPrkngInfoService/getPblcPrkngInfo";
    private static final int NUM_OF_ROWS = 100; // 한 번에 가져올 데이터 수
    private static final int TOTAL_COUNT = 615; // 총 데이터 수
    private static final int TOTAL_PAGES = (int) Math.ceil((double) TOTAL_COUNT / NUM_OF_ROWS);

    public void fetchAndSaveData() {
        RestTemplate restTemplate = new RestTemplate();
        for (int pageNo = 1; pageNo <= TOTAL_PAGES; pageNo++) {
            try {
                String apiUrl = buildUrl(pageNo, NUM_OF_ROWS);
                log.info("API URI: {}", apiUrl);

                URI uri = new URI(apiUrl);
                String responseString = restTemplate.getForObject(uri, String.class);

                JsonNode root = objectMapper.readTree(responseString);
                JsonNode items = root.path("response").path("body").path("items").path("item");

                List<ParkingData> parkingDataList = new ArrayList<>();
                for (JsonNode item : items) {
                    ParkingDataRequestDto dto = ParkingDataRequestDto.builder()
                            .guNm(item.path("guNm").asText())
                            .pkNam(item.path("pkNam").asText())
                            .mgntNum(item.path("mgntNum").asText())
                            .jibunAddr(item.path("jibunAddr").asText())
                            .pkFm(item.path("pkFm").asText())
                            .pkCnt(item.path("pkCnt").asInt())
                            .svcSrtTe(item.path("svcSrtTe").asText())
                            .svcEndTe(item.path("svcEndTe").asText())
                            .ldRtg(item.path("ldRtg").asText())
                            .tenMin(item.path("tenMin").asInt())
                            .ftDay(item.path("ftDay").asInt())
                            .ftMon(item.path("ftMon").asInt())
                            .xCdnt(item.path("xCdnt").asDouble())
                            .yCdnt(item.path("yCdnt").asDouble())
                            .fnlDt(item.path("fnlDt").asText())
                            .pkGubun(item.path("pkGubun").asText())
                            .build();

                    parkingDataList.add(ParkingDataMapper.toParkingData(dto));
                }

                repository.saveAll(parkingDataList);
            } catch (RestClientException e) {
                throw new RuntimeException(String.format("API에서 데이터를 가져오는 중 오류 발생 (페이지: %d): %s", pageNo, e.getMessage()), e);
            } catch (Exception e) {
                throw new RuntimeException(String.format("예기치 않은 오류 발생 (페이지: %d): %s", pageNo, e.getMessage()), e);
            }
        }
    }

    private String buildUrl(int pageNo, int numOfRows) {
        return BASE_URL + "?" + "serviceKey=" + SERVICE_KEY +
                "&pageNo=" + pageNo +
                "&numOfRows=" + numOfRows +
                "&resultType=json";
    }
}
