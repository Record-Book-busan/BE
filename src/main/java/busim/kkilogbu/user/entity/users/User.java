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


	private String username;
	private Long category;
	private LocalDateTime createdAt;
	private String ProfileImage;

	// 단방향으로 소셜 로그인 정보 참조
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")  // 외래키를 통해 연결
	private List<SocialLoginInfo> socialLoginInfos = new ArrayList<>();

	// 소셜 로그인 정보 추가
	public void addSocialLoginInfo(SocialLoginInfo socialLoginInfo) {
		this.socialLoginInfos.add(socialLoginInfo);
	}

	private String phoneIdentificationNumber;

	//약관 동의 체크
	private boolean agreePrivacy;

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


}
