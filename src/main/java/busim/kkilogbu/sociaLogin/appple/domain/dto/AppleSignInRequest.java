package busim.kkilogbu.sociaLogin.appple.domain.dto;

import lombok.Getter;

@Getter
public class AppleSignInRequest {

    private String authorizationCode;
    private String identityToken;
    private String phoneIdentificationNumber;
}
