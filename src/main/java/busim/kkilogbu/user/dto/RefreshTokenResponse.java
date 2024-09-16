package busim.kkilogbu.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RefreshTokenResponse {
    private String accessToken;   // 새로운 Access Token
    private String refreshToken;  // 기존의 Refresh Token (재사용 가능)
}
