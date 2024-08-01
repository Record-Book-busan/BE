package busim.kkilogbu.api.restroomAPI.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ToiletData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String toiletName; // 화장실명
    private String locationRoadNameAddress; // 소재지 도로명 주소
    private String locationJibunAddress; // 소재지 지번 주소
    private String unisexToilet; // 남녀 공용 화장실 여부
    private int maleToiletCount; // 남성용 대변기 수
    private int maleUrinalCount; // 남성용 소변기 수
    private int maleDisabledToiletCount; // 남성용 장애인용 대변기 수
    private int maleDisabledUrinalCount; // 남성용 장애인용 소변기 수
    private int maleChildToiletCount; // 남성용 어린이용 대변기 수
    private int maleChildUrinalCount; // 남성용 어린이용 소변기 수
    private int femaleToiletCount; // 여성용 대변기 수
    private int femaleDisabledToiletCount; // 여성용 장애인용 대변기 수
    private int femaleChildToiletCount; // 여성용 어린이용 대변기 수
    private String managementAgency; // 관리기관명
    private String phoneNumber; // 전화번호
    private String openingHours; // 개방 시간
    private String installationYear; // 설치년도
    private double latitude; // 위도
    private double longitude; // 경도

    @Builder
    public ToiletData(String toiletName, String locationRoadNameAddress, String locationJibunAddress,
                      String unisexToilet, int maleToiletCount, int maleUrinalCount, int maleDisabledToiletCount,
                      int maleDisabledUrinalCount, int maleChildToiletCount, int maleChildUrinalCount,
                      int femaleToiletCount, int femaleDisabledToiletCount, int femaleChildToiletCount,
                      String managementAgency, String phoneNumber, String openingHours, String installationYear,
                      double latitude, double longitude) {

        this.toiletName = toiletName;
        this.locationRoadNameAddress = locationRoadNameAddress;
        this.locationJibunAddress = locationJibunAddress;
        this.unisexToilet = unisexToilet;
        this.maleToiletCount = maleToiletCount;
        this.maleUrinalCount = maleUrinalCount;
        this.maleDisabledToiletCount = maleDisabledToiletCount;
        this.maleDisabledUrinalCount = maleDisabledUrinalCount;
        this.maleChildToiletCount = maleChildToiletCount;
        this.maleChildUrinalCount = maleChildUrinalCount;
        this.femaleToiletCount = femaleToiletCount;
        this.femaleDisabledToiletCount = femaleDisabledToiletCount;
        this.femaleChildToiletCount = femaleChildToiletCount;
        this.managementAgency = managementAgency;
        this.phoneNumber = phoneNumber;
        this.openingHours = openingHours;
        this.installationYear = installationYear;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}