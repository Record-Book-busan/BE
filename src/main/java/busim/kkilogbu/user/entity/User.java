package busim.kkilogbu.user.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.record.entity.Records;
import jakarta.persistence.*;
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

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String nickname;
	private Long category;
	private LocalDateTime createdAt;
	private String ProfileImage;

	private String appleUserId;  // 애플 사용자 ID
	private String email;        // 이메일
	private String refreshToken; // Refresh Token

	// 빌더 패턴을 사용한 생성자
	@Builder
	public User(Long id, String appleUserId, String email, String refreshToken) {
		this.id = id;
		this.appleUserId = appleUserId;
		this.email = email;
		this.refreshToken = refreshToken;
	}

	// Refresh Token만 업데이트하는 메서드 (Setter 대체)
	public User updateRefreshToken(String refreshToken) {
		return User.builder()
				.id(this.id)
				.appleUserId(this.appleUserId)
				.email(this.email)
				.refreshToken(refreshToken)
				.build();
	}

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
