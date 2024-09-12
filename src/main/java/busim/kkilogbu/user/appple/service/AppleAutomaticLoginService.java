package busim.kkilogbu.user.appple.service;

import busim.kkilogbu.user.appple.domain.dto.AppleTokenResponse;
import busim.kkilogbu.user.appple.domain.dto.LoginResponse;
import busim.kkilogbu.user.appple.domain.dto.SignInResponse;
import busim.kkilogbu.user.appple.service.AppleAuthService;
import busim.kkilogbu.user.appple.service.AppleTokenService;
import busim.kkilogbu.user.dto.SignInResponseMapper;
import busim.kkilogbu.user.entity.User;
import busim.kkilogbu.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleAutomaticLoginService {

    private final AppleAuthService appleAuthService;
    private final UserRepository userRepository;
    private final AppleTokenService appleTokenService;

    // 애플 자동 로그인 처리
    @Transactional
    public LoginResponse handleAutomaticLogin(String accessToken, User user) throws Exception {


        try {
            // 2. Access Token 검증 (유효하면 바로 반환)
            Claims claims = appleTokenService.verifyIdentityToken(accessToken);
            String appleUserId = claims.getSubject();

            if (appleUserId.equals(appleUserId)) {
                throw new IllegalArgumentException("유효하지 않은 애플 사용자 ID입니다.");
            }

            log.info("[애플 자동 로그인] Access Token 유효함. 유저ID: {}", user.getId());
            return LoginResponse.builder().accessToken(user.getAccessToken()).build();

        } catch (IllegalArgumentException e) {
            // 3. Access Token이 유효하지 않다면 Refresh Token을 사용해 재발급 시도
            log.warn("[애플 자동 로그인] Access Token 만료 또는 유효하지 않음. Refresh Token 사용하여 재발급 시도.");

            String refreshToken = user.getRefreshToken();
            if (refreshToken == null || refreshToken.isEmpty()) {
                throw new IllegalArgumentException("유효하지 않은 Access Token 이며, Refresh Token 이 없습니다.");
            }

            // 4. Refresh Token을 사용하여 새로운 Access Token 발급
            AppleTokenResponse tokenResponse = appleAuthService.getAccessTokenUsingRefreshToken(refreshToken);

            // 5. 새로운 Access Token과 Refresh Token을 유저 정보에 저장
            user.updateTokens(tokenResponse.refreshToken(), tokenResponse.accessToken());
            userRepository.save(user);

            log.info("[애플 자동 로그인] 새로운 Access Token 발급 성공. 유저ID: {}", user.getId());

            // 6. 새로 발급된 Access Token으로 로그인 정보 반환
            return LoginResponse.builder().accessToken(user.getAccessToken()).build();
        }
    }
}