package busim.kkilogbu.api.ParkingAPI.domain;

import busim.kkilogbu.api.ParkingAPI.domain.dto.ParkingDataRequestDto;
import busim.kkilogbu.api.ParkingAPI.domain.entity.ParkingData;

public class ParkingDataMapper {

    public static ParkingData toParkingData(ParkingDataRequestDto dto) {
        return ParkingData.builder()
                .guNm(dto.getGuNm())
                .pkNam(dto.getPkNam())
                .mgntNum(dto.getMgntNum())
                .jibunAddr(dto.getJibunAddr())
                .pkFm(dto.getPkFm())
                .pkCnt(dto.getPkCnt())
                .svcSrtTe(dto.getSvcSrtTe())
                .svcEndTe(dto.getSvcEndTe())
                .ldRtg(dto.getLdRtg())
                .tenMin(dto.getTenMin())
                .ftDay(dto.getFtDay())
                .ftMon(dto.getFtMon())
                .xCdnt(dto.getYCdnt()) // xCdnt와 yCdnt 위치 변경
                .yCdnt(dto.getXCdnt()) // xCdnt와 yCdnt 위치 변경
                .fnlDt(dto.getFnlDt())
                .pkGubun(dto.getPkGubun())
                .build();
    }

    public static ParkingDataRequestDto toParkingDataRequestDto(ParkingData entity) {
        return ParkingDataRequestDto.builder()
                .guNm(entity.getGuNm())
                .pkNam(entity.getPkNam())
                .mgntNum(entity.getMgntNum())
                .jibunAddr(entity.getJibunAddr())
                .pkFm(entity.getPkFm())
                .pkCnt(entity.getPkCnt())
                .svcSrtTe(entity.getSvcSrtTe())
                .svcEndTe(entity.getSvcEndTe())
                .ldRtg(entity.getLdRtg())
                .tenMin(entity.getTenMin())
                .ftDay(entity.getFtDay())
                .ftMon(entity.getFtMon())
                .xCdnt(entity.getYCdnt()) // xCdnt와 yCdnt 위치 변경
                .yCdnt(entity.getXCdnt()) // xCdnt와 yCdnt 위치 변경
                .fnlDt(entity.getFnlDt())
                .pkGubun(entity.getPkGubun())
                .build();
    }
}
