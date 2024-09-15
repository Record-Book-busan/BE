package busim.kkilogbu.user.entity.users;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.user.entity.BlackList;
import busim.kkilogbu.user.entity.LoginType;
import busim.kkilogbu.user.entity.UserInterest;
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

	private String email;

	private Long category;
	private LocalDateTime createdAt;
	private String ProfileImage;

	private String nickname;
	private String loginType;  // 소셜 로그인 유형 (애플, 구글, 카카오)

	private String socialUserId;  // 소셜 로그인 사용자 ID
	private String socialLoginAccessToken;   // 소셜 로그인 액세스 토큰
	private String socialLoginRefreshToken;  // 소셜 로그인 리프레시 토큰


	@OneToMany(mappedBy = "user")
	private List<Records> records = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Bookmark> bookmarks = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<BlackList> blackLists = new ArrayList<>();


	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private List<UserInterest> userInterests = new ArrayList<>();

	public void changeProfileImage(String profileImage) {
		this.ProfileImage = profileImage;
	}
	public void categoryChange(Long category) {
		this.category = category;
	}

	// 소셜 로그인 토큰 정보 업데이트 메소드
	public void updateTokens(String accessToken, String refreshToken) {
		this.socialLoginAccessToken = accessToken;
		this.socialLoginRefreshToken = refreshToken;
	}
}
