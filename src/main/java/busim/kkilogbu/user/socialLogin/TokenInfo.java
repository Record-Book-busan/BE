package busim.kkilogbu.user.socialLogin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenInfo {

    private final String grantType;         // 인증 유형, 보통 "Bearer"
    private final String accessToken;       // JWT 액세스 토큰
    private final String refreshToken;      // JWT 리프레시 토큰
    private final long accessTokenExpirationTime;   // 액세스 토큰의 만료 시간
    private final long refreshTokenExpirationTime;  // 리프레시 토큰의 만료 시간
}