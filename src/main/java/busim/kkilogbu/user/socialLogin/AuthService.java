package busim.kkilogbu.user.socialLogin;

import busim.kkilogbu.jwt.JwtTokenProvider;
import busim.kkilogbu.user.entity.Users;
import busim.kkilogbu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SocialLoginService socialLoginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public TokenInfo socialLogin(String provider, String token, String identityToken) throws IOException {
        // 소셜 로그인 전략 선택
        SocialLoginStrategy strategy = socialLoginService.getStrategy(provider);

        // 소셜 로그인 제공자로부터 사용자 정보 가져오기
        JSONObject userInfo = strategy.getUserInfo(provider.equalsIgnoreCase("apple") ? identityToken : token);

        // 사용자 이메일 추출
        String email = userInfo.optString("email", null);
        if (email == null) {
            throw new IllegalArgumentException("이메일 정보를 가져오지 못했습니다.");
        }

        // 사용자 정보 저장 또는 조회
        Users user = userRepository.findByEmail(email).orElseGet(() -> registerUser(email, provider, userInfo));

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), List.of("ROLE_USER"));
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // 사용자 엔티티에 리프레시 토큰 저장
        user.createRefreshToken(refreshToken);
        userRepository.save(user);

        // TokenInfo 객체 반환
        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Users registerUser(String email, String provider, JSONObject userInfo) {
        // 새로운 사용자 엔티티 생성 및 저장
        Users user = Users.builder()
                .email(email)
                .provider(provider)
                .username(userInfo.optString("name", "Unknown User"))
                .build();
        return userRepository.save(user);
    }
}
