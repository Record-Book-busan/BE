package busim.kkilogbu.api.ParkingAPI.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParkingDataRequestDto {
    //TODO : 관리자 폰번호 받아오면 좋을거 같음
    private String guNm;
    private String pkNam;
    private String mgntNum;
    private String jibunAddr;
    private String pkFm;
    private int pkCnt;
    private String svcSrtTe;
    private String svcEndTe;
    // TODO : 현재 평일 운영시간만 받아오는데 주말, 공휴일 운영시간도 받아와야 함
    private String ldRtg;
    private int tenMin;
    // TODO : 추가단위시간이랑 추가단위요금도 받아오면 좋을거 같음
    private int ftDay;
    private int ftMon;
    private double xCdnt;
    private double yCdnt;
    private String fnlDt;
    private String pkGubun;
    // TODO : 운영 요일, 특이사항을 받아와서 같이 표현하면 좋을거 같음
}
