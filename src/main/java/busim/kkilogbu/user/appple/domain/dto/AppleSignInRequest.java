package busim.kkilogbu.user.appple.domain.dto;

import lombok.Getter;

@Getter
public class AppleSignInRequest {

    private String authorizationCode;
    private String identityToken;
}
