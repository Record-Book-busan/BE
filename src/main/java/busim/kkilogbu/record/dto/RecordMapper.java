package busim.kkilogbu.record.dto;

import busim.kkilogbu.record.entity.Records;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecordMapper {
    public static RecordDetailResponse toCreateRecordDetailResponse(Records record, boolean bookMarked) {
        return RecordDetailResponse.builder()
                .id(record.getId())
                .title(record.getContents().getTitle())
                .content(record.getContents().getContent())
                .imageUrl(record.getContents().getImageUrl())
                .lat(record.getAddressInfo().getLatitude())
                .lng(record.getAddressInfo().getLongitude())
                .createdAt(record.getCreatedAt())
                .bookmarked(bookMarked)
                .build();
    }
}
