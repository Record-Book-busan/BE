package busim.kkilogbu.sociaLogin.appple.service;

import busim.kkilogbu.security.util.JwtUtil;
import busim.kkilogbu.sociaLogin.appple.domain.dto.AppleTokenResponse;
import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;

import busim.kkilogbu.user.dto.SignInResponseMapper;
import busim.kkilogbu.user.entity.LoginType;
import busim.kkilogbu.user.entity.users.Users;
import busim.kkilogbu.user.repository.UserConsentRepository;
import busim.kkilogbu.user.repository.UserRepository;
import busim.kkilogbu.user.util.NicknameGeneratorStrategy;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleLoginService {

    private final AppleTokenService appleTokenService;
    private final AppleAuthService appleAuthService;
    private final UserRepository userRepository;
    private final NicknameGeneratorStrategy nicknameGenerator;  // 닉네임 생성기
    private final JwtUtil jwtUtil;
    private final UserConsentRepository userConsentRepository;

    @Transactional
    public SignInResponse signInOrRegister(String authorizationCode, String identityToken) throws Exception {
        // 애플 로그인 처리 로직
        Claims claims = appleTokenService.verifyIdentityToken(identityToken);
        String appleUserId = claims.getSubject();

        log.info("애플 사용자 ID: " + appleUserId);

        // Access Token 교환
        AppleTokenResponse tokenResponse = appleAuthService.getAccessTokenUsingAuthCode(authorizationCode);

        // 사용자 조회 (존재하지 않으면 회원가입, 존재하면 로그인)
        Users users = userRepository.findBySocialUserId(appleUserId)
                .orElseGet(() -> registerNewUser(claims)); // 새 사용자만 회원가입

        String newAccessToken = jwtUtil.createAccessToken(String.valueOf(users.getId()));  // 회원 ID로 Access Token 생성
        String newRefreshToken = jwtUtil.createRefreshToken(String.valueOf(users.getId()));  // 회원 ID로 Refresh Token 생성

        // 사용자의 토큰 정보를 DB에 업데이트
        users.updateTokens(newAccessToken, newRefreshToken);

        // 약관 및 개인정보 처리 동의 여부 확인
        boolean isAgreed = userConsentRepository.existsByUsersAndTermsAgreedTrueAndPrivacyAgreedTrue(users);
        log.info("약관 및 개인정보 동의 상태: {}", isAgreed);

        // SignInResponseMapper를 사용해 SignInResponse 생성 및 반환
        SignInResponse signInResponse = SignInResponseMapper.createSignInResponse(users, newAccessToken, newRefreshToken, isAgreed);
        log.info("로그인 응답 반환 완료: 사용자 ID={}", users.getId());


        // 회원가입 또는 로그인 처리 후 응답 생성
        return signInResponse;
    }

    // 새로운 사용자 등록 (회원가입)
    private Users registerNewUser(Claims claims) {
        String email = claims.get("email", String.class);
        String nickname = nicknameGenerator.generateNickname();  // 닉네임 생성

        return userRepository.save(Users.builder()
                .email(email)   // 이메일 설정
                .nickname(nickname)  // 닉네임 설정
                .loginType(LoginType.APPLE.name())  // 로그인 유형 설정
                .socialUserId(claims.getSubject())  // 소셜 사용자 ID 설정
                .createdAt(LocalDateTime.now())  // 생성일자 설정
                        .role("USER")
                .build());
    }
}
