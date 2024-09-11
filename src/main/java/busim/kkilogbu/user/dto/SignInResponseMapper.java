package busim.kkilogbu.user.dto;

import busim.kkilogbu.user.appple.domain.dto.AppleTokenResponse;
import busim.kkilogbu.user.appple.domain.dto.SignInResponse;
import busim.kkilogbu.user.entity.User;

public class SignInResponseMapper {

    public static SignInResponse toSignInResponse(User user, AppleTokenResponse tokenResponse) {
        return SignInResponse.builder()
                .userId(user.getId())
                .accessToken(tokenResponse.accessToken())
                .refreshToken(tokenResponse.refreshToken())
                .build();
    }
}
