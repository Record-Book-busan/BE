package busim.kkilogbu.api.ParkingAPI.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParkingDataRequestDto {

    private String guNm;
    private String pkNam;
    private String mgntNum;
    private String jibunAddr;
    private String pkFm;
    private int pkCnt;
    private String svcSrtTe;
    private String svcEndTe;
    private String ldRtg;
    private int tenMin;
    private int ftDay;
    private int ftMon;
    private double xCdnt;
    private double yCdnt;
    private String fnlDt;
    private String pkGubun;
}
