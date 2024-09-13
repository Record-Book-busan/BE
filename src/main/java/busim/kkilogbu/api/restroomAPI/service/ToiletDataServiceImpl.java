package busim.kkilogbu.api.restroomAPI.service;

import busim.kkilogbu.api.restroomAPI.domain.ToiletMapper;
import busim.kkilogbu.api.restroomAPI.domain.dto.ToiletDataResponse;
import busim.kkilogbu.api.restroomAPI.domain.entity.ToiletData;
import busim.kkilogbu.api.restroomAPI.repository.ToiletDataRepository;
import busim.kkilogbu.global.Ex.ToiletNotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class ToiletDataServiceImpl implements ToiletDataService {

    public  final ToiletDataRepository toiletDataRepository;

    @Transactional
    public void saveCSVData(MultipartFile file) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<ToiletData> toiletDataList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                ToiletData toiletData = ToiletData.builder()
                        .toiletName(csvRecord.get("화장실명"))
                        .locationRoadNameAddress(csvRecord.get("소재지도로명주소"))
                        .locationJibunAddress(csvRecord.get("소재지지번주소"))
                        .maleToiletCount(parseInteger(csvRecord.get("남성용-대변기수")))
                        .maleUrinalCount(parseInteger(csvRecord.get("남성용-소변기수")))
                        .maleDisabledToiletCount(parseInteger(csvRecord.get("남성용-장애인용대변기수")))
                        .maleDisabledUrinalCount(parseInteger(csvRecord.get("남성용-장애인용소변기수")))
                        .maleChildToiletCount(parseInteger(csvRecord.get("남성용-어린이용대변기수")))
                        .maleChildUrinalCount(parseInteger(csvRecord.get("남성용-어린이용소변기수")))
                        .femaleToiletCount(parseInteger(csvRecord.get("여성용-대변기수")))
                        .femaleDisabledToiletCount(parseInteger(csvRecord.get("여성용-장애인용대변기수")))
                        .femaleChildToiletCount(parseInteger(csvRecord.get("여성용-어린이용대변기수")))
                        .managementAgency(csvRecord.get("관리기관명"))
                        .phoneNumber(csvRecord.get("전화번호"))
                        .openingHours(csvRecord.get("개방시간"))
                        .installationYear(csvRecord.get("설치연월"))
                        .latitude(parseDouble(csvRecord.get("WGS84위도")))
                        .longitude(parseDouble(csvRecord.get("WGS84경도")))
                        .build();

                toiletDataList.add(toiletData);
            }

            toiletDataRepository.saveAll(toiletDataList);
        } catch (IOException e) {
            throw new RuntimeException("CSV 파일을 처리하는 동안 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty() || value.equals("-")) {
            return 0; // 기본값으로 0을 반환하거나 적절한 값을 설정하세요.
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0; // 기본값으로 0을 반환하거나 적절한 값을 설정하세요.
        }
    }

    private Double parseDouble(String value) {
        if (value == null || value.trim().isEmpty() || value.equals("-")) {
            return 0.0; // 기본값으로 0.0을 반환하거나 적절한 값을 설정하세요.
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0; // 기본값으로 0.0을 반환하거나 적절한 값을 설정하세요.
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ToiletDataResponse> getToiletsWithinRadius(double latitude, double longitude) {
        List<ToiletData> toilets = toiletDataRepository.findToiletsWithinRadius(latitude, longitude);
        if (toilets.isEmpty()) {
            throw new ToiletNotFoundException();
        }
        return toilets.stream()
                .map(ToiletMapper::toToiletData)
                .collect(Collectors.toList());
    }
}
