package busim.kkilogbu.user.entity.users;


import busim.kkilogbu.user.entity.LoginType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;  // 사용자와 연관관계

    private String nickname;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;  // 소셜 로그인 유형 (애플, 구글, 카카오)

    private String socialUserId;  // 소셜 로그인 사용자 ID
    private String accessToken;   // 소셜 로그인 액세스 토큰
    private String refreshToken;  // 소셜 로그인 리프레시 토큰

    public SocialLoginInfo(LoginType loginType, String socialUserId, String accessToken, String refreshToken, String nickname) {
        this.loginType = loginType;
        this.socialUserId = socialUserId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
    }

    public void updateTokens(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}