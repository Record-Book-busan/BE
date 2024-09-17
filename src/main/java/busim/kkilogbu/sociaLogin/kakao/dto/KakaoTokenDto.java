package busim.kkilogbu.sociaLogin.kakao.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoTokenDto {

    private String accessToken;
    private Long accessTokenExpiresAt;
    private String refreshToken;
    private Long refreshTokenExpiresAt;


}
