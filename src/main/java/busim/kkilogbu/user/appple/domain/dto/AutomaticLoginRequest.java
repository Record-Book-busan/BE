package busim.kkilogbu.user.appple.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AutomaticLoginRequest {

    private String deviceIdentificationCode;
    private String accessToken;

}
