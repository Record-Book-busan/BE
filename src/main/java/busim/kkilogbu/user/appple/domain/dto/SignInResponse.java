package busim.kkilogbu.user.appple.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {

    private Long userId;
    private String accessToken;
    private String refreshToken;
}
