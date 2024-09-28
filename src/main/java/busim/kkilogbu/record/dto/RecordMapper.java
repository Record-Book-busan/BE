package busim.kkilogbu.record.dto;

import busim.kkilogbu.record.entity.Records;

public class RecordMapper {

    public static RecordDetailResponse toCreateRecordDetailResponse(Records record) {
        return RecordDetailResponse.builder()
                .id(record.getId())
                .title(record.getContents().getTitle())
                .content(record.getContents().getContent())
                .imageUrl(record.getContents().getImageUrl())
                .address(record.getAddressInfo().getAddress())
                .lat(record.getAddressInfo().getLatitude())
                .lng(record.getAddressInfo().getLongitude())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
