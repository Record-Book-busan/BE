package busim.kkilogbu.record.entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import busim.kkilogbu.addressInfo.entity.AddressInfo;
import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.contents.entity.Contents;
import busim.kkilogbu.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Records {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;


	@CreatedDate
	private String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

	@OneToOne(mappedBy = "records", fetch = LAZY, cascade = PERSIST, orphanRemoval = true)
	private Contents contents;

	//TODO : delete 시 addressInfo 삭제되는지 확인
	@ManyToOne(fetch = LAZY, cascade = PERSIST)
	@JoinColumn(name = "address_info_id")
	private AddressInfo addressInfo;

	// TODO : bookmark 구현 후 다시 작업
	@OneToMany(mappedBy = "records"
		// , cascade = PERSIST
		, orphanRemoval = true
	)
	private List<Bookmark> bookmark = new ArrayList<>();

	public void connect(User user, AddressInfo addressInfo, Contents contents) {
		// TODO : 로그인 구현시 추가
		this.user = user;
		user.getRecords().add(this);
		this.addressInfo = addressInfo;
		addressInfo.getRecords().add(this);
		this.contents = contents;
		contents.connect(this, null);
	}
	public void connect(AddressInfo addressInfo){
		this.addressInfo = addressInfo;
		addressInfo.getRecords().add(this);
	}


	public static Records createRecord(User user, AddressInfo addressInfo, Contents contents) {
		Records record = new Records();  // Records 객체 생성
		// record.user = user;
		// user.getRecords().add(record);
		record.addressInfo = addressInfo;
		record.contents = contents;
		contents.connect(record, null);
		addressInfo.getRecords().add(record);
		return record;
	}


}
