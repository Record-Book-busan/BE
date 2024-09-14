package busim.kkilogbu.sociaLogin.appple.service;

import busim.kkilogbu.sociaLogin.appple.domain.dto.AppleTokenResponse;
import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;
import busim.kkilogbu.sociaLogin.appple.service.AppleAuthService;
import busim.kkilogbu.sociaLogin.appple.service.AppleTokenService;
import busim.kkilogbu.user.dto.SignInResponseMapper;
import busim.kkilogbu.user.entity.LoginType;
import busim.kkilogbu.user.entity.users.SocialLoginInfo;
import busim.kkilogbu.user.entity.users.User;
import busim.kkilogbu.user.repository.UserRepository;
import busim.kkilogbu.user.util.NicknameGeneratorStrategy;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleLoginService {

    private final AppleTokenService appleTokenService;
    private final AppleAuthService appleAuthService;
    private final UserRepository userRepository;
    private final NicknameGeneratorStrategy nicknameGenerator;  // 닉네임 생성기 의존성 추가

    @Transactional
    public SignInResponse signInOrRegister(String authorizationCode, String identityToken) throws Exception {
        // 애플 로그인 처리 로직
        Claims claims = appleTokenService.verifyIdentityToken(identityToken);
        String appleUserId = claims.getSubject();

        log.info("애플 사용자 ID: " + appleUserId);

        // Access Token 교환
        AppleTokenResponse tokenResponse = appleAuthService.getAccessTokenUsingAuthCode(authorizationCode);

        // 사용자 조회 (존재하지 않으면 회원가입, 존재하면 로그인)
        User user = userRepository.findByAppleUserId(appleUserId)
                .orElseGet(() -> registerNewUser(claims)); // 새 사용자만 회원가입

        // 토큰 정보만 업데이트 (기본 정보는 변경하지 않음)
        updateOrAddSocialLoginInfo(user, LoginType.APPLE, appleUserId, tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        // 회원가입 또는 로그인 처리 후 응답 생성
        return SignInResponseMapper.toSignInResponse(user, tokenResponse);
    }

    // 새로운 사용자 등록 (회원가입)
    private User registerNewUser(Claims claims) {
        String email = claims.get("email", String.class);

        return userRepository.save(User.builder()
                .email(email)   // 이메일 설정
                .build());
    }

    // 소셜 로그인 정보 추가 또는 업데이트
    private void updateOrAddSocialLoginInfo(User user, LoginType loginType, String socialUserId, String accessToken, String refreshToken) {
        boolean exists = user.getSocialLoginInfos().stream()
                .anyMatch(info -> info.getLoginType() == loginType && info.getSocialUserId().equals(socialUserId));

        if (!exists) {
            String nickname = nicknameGenerator.generateNickname();  // 닉네임 생성기 호출
            SocialLoginInfo socialLoginInfo = new SocialLoginInfo(loginType, socialUserId, accessToken, refreshToken, nickname);
            user.addSocialLoginInfo(socialLoginInfo);  // 새로운 소셜 로그인 정보 추가
        } else {
            user.getSocialLoginInfos().stream()
                    .filter(info -> info.getLoginType() == loginType && info.getSocialUserId().equals(socialUserId))
                    .forEach(info -> {
                        info.updateTokens(accessToken, refreshToken);  // 토큰 정보만 업데이트
                    });
        }

        // 변경된 사용자 정보 저장
        userRepository.save(user);
    }
}
