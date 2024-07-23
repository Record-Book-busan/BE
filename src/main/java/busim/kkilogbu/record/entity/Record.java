package busim.kkilogbu.record.entity;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.addressInfo.entity.AddressInfo;
import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.contents.entity.Contents;
import busim.kkilogbu.global.Category1;
import busim.kkilogbu.user.entity.User;
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

@Entity
@Table
@Builder
@Getter
public class Record {
	@Id @GeneratedValue
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@Enumerated(STRING)
	private Category1 cat1;
	private Long cat2;
	private LocalDateTime createdAt;
	@OneToOne(mappedBy = "record", fetch = LAZY)
	private Contents contents;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "address_info_id")
	private AddressInfo addressInfo;

	@OneToMany(mappedBy = "record")
	private List<Bookmark> bookmark = new ArrayList<>();

	public Record() {

	}
}
