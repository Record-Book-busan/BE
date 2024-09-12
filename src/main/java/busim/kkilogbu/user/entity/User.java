package busim.kkilogbu.user.entity;



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
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String nickname;
	private Long category;
	private LocalDateTime createdAt;
	private String ProfileImage;

	@Column(length = 1000)
	private String appleUserId;  // 애플 사용자 ID
	@Column(length = 1000)
	private String email;        // 이메일
	@Column(length = 1000)
	private String refreshToken; // Refresh Token

	@Column(length = 1000)
	private String accessToken;  // 액세스 토큰

	private String phoneIdentificationNumber;

	//약관 동의 체크
	private boolean agreePrivacy;

	@OneToMany(mappedBy = "user")
	private List<Records> records = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Bookmark> bookmarks = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<BlackList> blackLists = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private LoginType loginType;

	public void updateTokens(String refreshToken,String accessToken){
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
	}

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
