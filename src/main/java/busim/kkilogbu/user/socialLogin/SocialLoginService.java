package busim.kkilogbu.user.socialLogin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final GoogleLoginStrategy googleLoginStrategy;
//    private final KakaoLoginStrategy kakaoLoginStrategy;
//    private final AppleLoginStrategy appleLoginStrategy;

    /**
     * 소셜 로그인 제공자에 따라 적절한 전략을 반환합니다.
     *
     * @param provider 소셜 로그인 제공자 (예: "google", "kakao", "apple")
     * @return 해당 소셜 로그인 제공자에 맞는 SocialLoginStrategy
     */
    public SocialLoginStrategy getStrategy(String provider) {
        switch (provider.toLowerCase()) {
            case "google":
                return googleLoginStrategy;
//            case "kakao":
//                return kakaoLoginStrategy;
//            case "apple":
//                return appleLoginStrategy;
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 로그인 제공자입니다: " + provider);
        }
    }
}
