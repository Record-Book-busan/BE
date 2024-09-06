package busim.kkilogbu.addressInfo.entity;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.record.entity.Records;
import org.springframework.util.StringUtils;

import busim.kkilogbu.place.entity.Place;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = PROTECTED)
@Getter
public class AddressInfo {
	@Id @GeneratedValue
	private Long id;
	private String address;
	private String addressDetail;
	private String zipcode;
	// TODO : 위도 경도 범위를 부산으로 한정하면 좋을거 같음
	private double latitude;
	private double longitude;

	@OneToMany(mappedBy = "addressInfo")
	private List<Records> records = new ArrayList<>();
	@OneToMany(mappedBy = "addressInfo")
	private List<Place> place = new ArrayList<>();

	@Builder
	public AddressInfo(String address, String addressDetail, String zipcode, double latitude, double longitude) {
		this.address = address;
		this.addressDetail = addressDetail;
		this.zipcode = zipcode;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void update(String address, String addressDetail, String zipcode, double lat, double lng) {
		if(StringUtils.hasText(address)) this.address = address;
		if(StringUtils.hasText(addressDetail)) this.addressDetail = addressDetail;
		if(StringUtils.hasText(zipcode)) this.zipcode = zipcode;
		if(!Double.isNaN(lat)) this.latitude = lat;
		if(!Double.isNaN(lng)) this.longitude = lng;
	}
}
