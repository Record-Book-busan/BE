package busim.kkilogbu.sociaLogin.appple.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AppleTokenRequest {

    private String clientId;
    private String clientSecret;
    private String code;
    private String grantType;
    private String refreshToken;
}
