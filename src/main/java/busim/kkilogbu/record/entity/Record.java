package busim.kkilogbu.record.entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

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
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Record {
	@Id @GeneratedValue
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(STRING)
	private Category1 cat1;
	private Long cat2;
	@CreatedDate
	private String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

	@OneToOne(mappedBy = "record", fetch = LAZY, cascade = PERSIST, orphanRemoval = true)
	private Contents contents;

	//TODO : delete 시 addressInfo 삭제되는지 확인
	@ManyToOne(fetch = LAZY, cascade = PERSIST)
	@JoinColumn(name = "address_info_id")
	private AddressInfo addressInfo;

	// TODO : bookmark 구현 후 다시 작업
	@OneToMany(mappedBy = "record"
		// , cascade = PERSIST
		, orphanRemoval = true
	)
	private List<Bookmark> bookmark = new ArrayList<>();

	public void connect(User user, AddressInfo addressInfo, Contents contents) {
		// TODO : 로그인 구현시 추가
		// this.user = user;
		// user.getRecords().add(this);
		this.addressInfo = addressInfo;
		addressInfo.getRecord().add(this);
		this.contents = contents;
		contents.connect(this, null);
	}
	public void connect(AddressInfo addressInfo){
		this.addressInfo = addressInfo;
		addressInfo.getRecord().add(this);
	}

	@Builder
	public Record(Category1 cat1, Long cat2) {
		this.cat1 = cat1;
		this.cat2 = cat2;
	}

	public void update(Category1 cat1, Long cat2) {
		if(cat1 != null) this.cat1 = cat1;
		if(cat2 != null) this.cat2 = cat2;
	}
}
