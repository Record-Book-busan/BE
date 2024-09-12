package busim.kkilogbu.user.appple.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserConsentRequest {

    private boolean termsAgreed;      // 이용약관 동의 여부
    private boolean privacyAgreed;    // 개인정보 처리 동의 여부

}
