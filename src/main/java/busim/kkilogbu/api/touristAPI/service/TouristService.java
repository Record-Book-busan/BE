package busim.kkilogbu.api.touristAPI.service;

import busim.kkilogbu.api.touristAPI.domain.entity.Tourist;

import busim.kkilogbu.api.touristAPI.repository.TouristRepository;
import busim.kkilogbu.place.dto.TouristCategory;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class TouristService {

    private final TouristRepository touristRepository;

    public void saveTouristsFromCSV(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())) {

            List<Tourist> tourists = new ArrayList<>();
            for (CSVRecord csvRecord : csvParser.getRecords()) {
                Tourist tourist = Tourist.builder()
                        .name(csvRecord.get("장소"))
                        .location(csvRecord.get("위치"))
                        .imageUrl(csvRecord.get("이미지"))
                        .longitude(Double.parseDouble(csvRecord.get("경도")))
                        .latitude(Double.parseDouble(csvRecord.get("위도")))
                        .categoryLarge(TouristCategory.fromString(csvRecord.get("대분류")))  // 대분류 변환
                        .categoryMedium(csvRecord.get("중분류"))
                        .categorySmall(csvRecord.get("소분류"))
                        .build();

                tourists.add(tourist);
            }

            touristRepository.saveAll(tourists);

        } catch (Exception e) {
            throw new RuntimeException("CSV 파일을 처리하는 동안 오류가 발생했습니다.", e);
        }
    }
}
