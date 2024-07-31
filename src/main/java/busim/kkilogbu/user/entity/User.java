package busim.kkilogbu.user.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.record.entity.Record;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = PROTECTED)
@Getter
public class User {
	@Id @GeneratedValue
	private Long id;
	private String username;
	private String nickname;
	private Long category;
	private LocalDateTime createdAt;

	//약관 동의 체크
	private boolean agreePrivacy;

	@OneToMany(mappedBy = "user")
	private List<Record> records = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Bookmark> bookmarks = new ArrayList<>();
	@Builder
	public User(String username, String nickname, Long category, LocalDateTime createdAt, boolean agreePrivacy) {
		this.username = username;
		this.nickname = nickname;
		this.category = category;
		this.createdAt = createdAt;
		this.agreePrivacy = agreePrivacy;
	}
}
