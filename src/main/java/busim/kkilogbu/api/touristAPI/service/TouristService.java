package busim.kkilogbu.api.touristAPI.service;

import busim.kkilogbu.api.touristAPI.domain.entity.Tourist;
import busim.kkilogbu.api.touristAPI.repository.TouristRepository;
import busim.kkilogbu.place.dto.TouristCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TouristService {

    private final TouristRepository touristRepository;

    public void saveTouristsFromCSV(MultipartFile file) {
        log.info("CSV 파일 처리 시작: {}", file.getOriginalFilename());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())) {

            List<Tourist> tourists = new ArrayList<>();
            int recordCount = 0;

            for (CSVRecord csvRecord : csvParser.getRecords()) {
                try {
                    Tourist tourist = Tourist.builder()
                            .name(csvRecord.get("장소"))
                            .location(csvRecord.get("위치"))
                            .imageUrl(csvRecord.get("이미지"))
                            .longitude(Double.parseDouble(csvRecord.get("경도")))
                            .latitude(Double.parseDouble(csvRecord.get("위도")))
                            .categoryLarge(csvRecord.get("대분류"))  // 대분류 변환
                            .categoryMedium(csvRecord.get("중분류"))
                            .categorySmall(csvRecord.get("소분류"))
                            .build();

                    tourists.add(tourist);
                    recordCount++;
                } catch (Exception e) {
                    log.error("레코드 처리 중 오류 발생 (레코드 번호: {}): {}", csvRecord.getRecordNumber(), e.getMessage());
                }
            }

            log.info("총 {}개의 기록을 CSV에서 처리했습니다.", recordCount);

            if (!tourists.isEmpty()) {
                touristRepository.saveAll(tourists);
                log.info("{}개의 관광지 정보를 데이터베이스에 저장했습니다.", tourists.size());
            } else {
                log.warn("저장할 관광지 정보가 없습니다.");
            }

        } catch (Exception e) {
            log.error("CSV 파일을 처리하는 동안 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("CSV 파일을 처리하는 동안 오류가 발생했습니다.", e);
        }
    }
}
