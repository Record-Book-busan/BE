package busim.kkilogbu.place.entity;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.addressInfo.entity.AddressInfo;
import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.contents.entity.Contents;
import busim.kkilogbu.global.Category1;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Place {
	@Id @GeneratedValue
	private Long id;
	@Enumerated(STRING)
	private Category1 cat1;
	private Long cat2;
	@OneToOne(mappedBy = "place", fetch = LAZY)
	private Contents contents;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "address_info_id")
	private AddressInfo addressInfo;
	private String operatingTime;
	private String phone;

	@OneToMany(mappedBy = "place")
	private List<Bookmark> bookmark = new ArrayList<>();

	@Builder
	public Place(Category1 cat1, Long cat2, String operatingTime, String phone) {
		this.cat1 = cat1;
		this.cat2 = cat2;
		this.operatingTime = operatingTime;
		this.phone = phone;
	}
}
