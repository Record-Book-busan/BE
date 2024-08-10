package busim.kkilogbu.api.ParkingAPI.domain.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ParkingDataResponse {
	private Long id;
	private double lat;
	private double lng;
	private String jibunAddr;	// 소재지 지번 주소
	private String pkFm;		// 주차장 종류(노상, 노외)
	private int pkCnt;			// 주차 면(주차 가능 차량 수)
	private String svcSrtTe;	// 평일 운영 시작 시간
	private String svcEndTe;	// 평일 운영 종료 시간
	private int tenMin;			// 10분당 요금
	private int ftDay;			// 1일 주차권 요금
	private int ftMon;			// 월 정기권 요금
	private String pkGubun;		// 주차장 종류(공영, 민영, 무료)
}
