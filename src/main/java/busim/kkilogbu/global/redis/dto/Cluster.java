package busim.kkilogbu.global.redis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "위치 클러스터 정보를 담고 있는 객체")
public class Cluster {

	@Schema(description = "위도", example = "37.5665")
	private double lat;

	@Schema(description = "경도", example = "126.9780")
	private double lng;

	@Schema(description = "해당 위치에 포함된 장소들의 리스트")
	private List<Object> places;

	@Builder
	public Cluster(double lat, double lng, List<Object> places) {
		this.lat = lat;
		this.lng = lng;
		this.places = places;
	}
}
