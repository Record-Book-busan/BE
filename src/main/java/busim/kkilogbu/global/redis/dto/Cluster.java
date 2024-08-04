package busim.kkilogbu.global.redis.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Cluster {
	private double lat;
	private double lng;
	private List<Object> places;
	@Builder
	public Cluster(double lat, double lng, List<Object> places) {
		this.lat = lat;
		this.lng = lng;
		this.places = places;
	}
}
