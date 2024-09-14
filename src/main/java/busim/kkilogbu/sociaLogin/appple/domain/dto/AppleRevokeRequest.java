package busim.kkilogbu.sociaLogin.appple.domain.dto;


import lombok.Builder;


@Builder
public class AppleRevokeRequest {
    private String clientId;  // 애플 클라이언트 ID (Bundle ID)
    private String clientSecret;  // JWT로 서명된 클라이언트 비밀 키
    private String token;  // 해지할 토큰 (access_token 또는 refresh_token)
    private String tokenTypeHint;  // "access_token" 또는 "refresh_token"
}