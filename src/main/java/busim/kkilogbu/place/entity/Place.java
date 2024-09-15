package busim.kkilogbu.place.entity;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.addressInfo.entity.AddressInfo;
import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;
import busim.kkilogbu.api.touristAPI.domain.entity.Tourist;
import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.contents.entity.Contents;
import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Place {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(mappedBy = "place", fetch = LAZY)
	private Contents contents;

	// addressInfo와 연관 관계 설정
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "address_info_id")
	private AddressInfo addressInfo;

	@OneToMany(mappedBy = "place")
	private List<Bookmark> bookmark = new ArrayList<>();



	// Builder 패턴으로 객체 생성
	@Builder
	public Place(AddressInfo addressInfo, Contents contents, Tourist tourist, Restaurant restaurant) {
		this.addressInfo = addressInfo;
		this.contents = contents;

	}
}
