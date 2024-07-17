package busim.kkilogbu.addressInfo.entity;

import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.map.entity.Map;
import busim.kkilogbu.record.entity.Record;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class AddressInfo {
	@Id @GeneratedValue
	private Long id;
	private String address;
	private String addressDetail;
	private String zipcode;
	private double latitude;
	private double longitude;

	@OneToMany(mappedBy = "addressInfo")
	private List<Record> record = new ArrayList<>();
	@OneToMany(mappedBy = "addressInfo")
	private List<Map> map = new ArrayList<>();
}
