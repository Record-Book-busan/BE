package busim.kkilogbu.api.ParkingAPI.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ParkingData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
