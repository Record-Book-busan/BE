package busim.kkilogbu.sociaLogin.kakao.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoTokenDto {

    //회원 번호
    @JsonProperty("id")
    public Long id;

    // 액세스 토큰
    @JsonProperty("access_token")
    private String accessToken;

    // 리프레시 토큰
    @JsonProperty("refresh_token")
    private String refreshToken;

    // 액세스 토큰 만료 시간 (초 단위)
    @JsonProperty("expires_in")
    private Long expiresIn;

    // 리프레시 토큰 만료 시간 (초 단위)
    @JsonProperty("refresh_token_expires_in")
    private Long refreshTokenExpiresIn;

    // 토큰 타입 (일반적으로 Bearer)
    @JsonProperty("token_type")
    private String tokenType;

    // 스코프(토큰 권한)
    @JsonProperty("scope")
    private String scope;

    // 카카오 계정 정보
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {

        // 사용자 프로필 정보
        @JsonProperty("profile")
        private Profile profile;

        // 이메일 정보
        @JsonProperty("email")
        private String email;

        // 성별 정보
        @JsonProperty("gender")
        private String gender;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Profile {
            @JsonProperty("nickname")
            private String nickName;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }
}
