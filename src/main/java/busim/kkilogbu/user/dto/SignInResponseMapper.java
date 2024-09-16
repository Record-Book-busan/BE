package busim.kkilogbu.user.dto;

import busim.kkilogbu.sociaLogin.appple.domain.dto.AppleTokenResponse;
import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;
import busim.kkilogbu.user.entity.users.User;
import lombok.Getter;

@Getter
public class SignInResponseMapper {

    public static SignInResponse toSignInResponse(User user,String accessToken,String refreshToken) {
        return SignInResponse.builder()
                .userId(user.getId())
                .accessToken("Bearer " + accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
