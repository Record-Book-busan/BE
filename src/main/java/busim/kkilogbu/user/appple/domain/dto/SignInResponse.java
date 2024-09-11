package busim.kkilogbu.user.appple.domain.dto;

import lombok.Builder;

@Builder
public class SignInResponse {

    private Long userId;
    private String accessToken;
    private String refreshToken;
}
