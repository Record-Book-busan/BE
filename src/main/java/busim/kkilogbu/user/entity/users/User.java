package busim.kkilogbu.user.entity.users;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.user.entity.BlackList;
import busim.kkilogbu.user.entity.UserInterest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
	private String loginType;
	private String socialUserId;
	private String accessToken;
	private String refreshToken;
	private String role;

	@Builder.Default
	@OneToMany(mappedBy = "user")
	private List<Records> records = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "user")
	private List<Bookmark> bookmarks = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "user")
	private List<BlackList> blackLists = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private List<UserInterest> userInterests = new ArrayList<>();

	public void changeProfileImage(String profileImage) {
		this.ProfileImage = profileImage;
	}

	public void categoryChange(Long category) {
		this.category = category;
	}

	public void updateTokens(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
