package busim.kkilogbu.user.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.record.entity.Records;
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
@Builder
@AllArgsConstructor
public class User {
	@Id @GeneratedValue
	private Long id;
	private String username;
	private String nickname;
	private Long category;
	private LocalDateTime createdAt;
	private String ProfileImage;

	//약관 동의 체크
	private boolean agreePrivacy;

	@OneToMany(mappedBy = "user")
	private List<Records> records = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Bookmark> bookmarks = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<BlackList> blackLists = new ArrayList<>();

	public void changeNickname(String nickname) {
		this.nickname = nickname;
	}
	public void changeProfileImage(String profileImage) {
		this.ProfileImage = profileImage;
	}
	public void categoryChange(Long category) {
		this.category = category;
	}


}
