package busim.kkilogbu.user.dto;

import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;
import busim.kkilogbu.user.entity.users.Users;
import lombok.Getter;

@Getter
public class SignInResponseMapper {

    public static SignInResponse toSignInResponse(Users users, String accessToken, String refreshToken) {
        return SignInResponse.builder()
                .userId(users.getId())
                .accessToken("Bearer " + accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
