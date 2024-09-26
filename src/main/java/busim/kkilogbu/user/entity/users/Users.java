package busim.kkilogbu.user.entity.users;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.user.entity.BlackList;
import busim.kkilogbu.user.entity.UserConsent;
import busim.kkilogbu.user.entity.UserInterest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;
	private Long category;
	private LocalDateTime createdAt;
	private String profileImage;
	private String nickname;
	private String loginType;

	@Column(length = 1000)
	private String socialUserId;

	@Column(length = 1000)
	private String accessToken;

	@Column(length = 1000)
	private String refreshToken;

	private String role;


	@OneToMany(mappedBy = "users")
	private List<Records> records = new ArrayList<>();


	@OneToMany(mappedBy = "users")
	private List<Bookmark> bookmarks = new ArrayList<>();


	@OneToMany(mappedBy = "users")
	private List<BlackList> blackLists = new ArrayList<>();


	@OneToMany(mappedBy = "users", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private List<UserInterest> userInterests = new ArrayList<>();


	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
	private List<UserConsent> userConsents = new ArrayList<>();

	public void changeProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void changeNickname(String nickname) {
		this.nickname = nickname;
	}

	public void categoryChange(Long category) {
		this.category = category;
	}

	public void updateTokens(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public void kakaoSignin(String socialUserId, String email, String nickname, String profileImage){
		this.socialUserId = socialUserId;
		this.email = email;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.loginType = "KAKAO";
		this.role = "USER";
	}


}
