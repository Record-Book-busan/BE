package busim.kkilogbu.user.dto;

import busim.kkilogbu.sociaLogin.appple.domain.dto.AppleTokenResponse;
import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;
import busim.kkilogbu.user.entity.users.User;
import lombok.Getter;

@Getter
public class SignInResponseMapper {

    public static SignInResponse toSignInResponse(User user, AppleTokenResponse tokenResponse) {
        return SignInResponse.builder()
                .userId(user.getId())
                .accessToken("Bearer " + tokenResponse.accessToken())
                .refreshToken(tokenResponse.refreshToken())
                .build();
    }
}
