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
@Builder
@AllArgsConstructor
public class Users {
	@Id @GeneratedValue
	private Long id;
	private String email;
	private String provider;
	private String username;
	private String nickname;
	private Long category;
	private LocalDateTime createdAt;
	private String refreshToken;

	public void createRefreshToken(String refreshToken){
		if(refreshToken != null){
			this.refreshToken = refreshToken;
		}
	}
	//약관 동의 체크
	private boolean agreePrivacy;

	@OneToMany(mappedBy = "users")
	private List<Record> records = new ArrayList<>();

	@OneToMany(mappedBy = "users")
	private List<Bookmark> bookmarks = new ArrayList<>();

}
