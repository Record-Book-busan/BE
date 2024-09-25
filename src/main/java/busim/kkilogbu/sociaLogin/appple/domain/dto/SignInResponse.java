package busim.kkilogbu.sociaLogin.appple.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {

    private Long userId;
    private String accessToken;
    private String refreshToken;
    private Instant accessTokenAt;
}
