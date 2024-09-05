package busim.kkilogbu.global.redis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "위치 클러스터 정보를 담고 있는 객체")
public class Cluster<T> {

	@Schema(description = "위도", example = "37.5665")
	private double lat;

	@Schema(description = "경도", example = "126.9780")
	private double lng;

	@Schema(description = "해당 위치에 포함된 기록데이터 리스트")
	private List<T> recordData;  // 제네릭 타입으로 변경

	@Builder
	public Cluster(double lat, double lng, List<T> recordData) {
		this.lat = lat;
		this.lng = lng;
		this.recordData = recordData;
	}
}
