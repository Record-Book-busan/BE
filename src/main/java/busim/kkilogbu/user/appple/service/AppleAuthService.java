package busim.kkilogbu.user.appple.service;

import busim.kkilogbu.user.appple.controller.AppleClient;
import busim.kkilogbu.user.appple.domain.dto.AppleTokenRequest;
import busim.kkilogbu.user.appple.domain.dto.AppleTokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleAuthService {

    private final String keyId = "J8B4BN5ZKY";  // 애플 Key ID
    private final String teamId = "RRY6QV29NW";  // 애플 Team ID
    private final String clientId = "com.busim.recordbookbusan";  // 애플 Client ID (Bundle ID)
    private final String authUrl = "https://appleid.apple.com";  // 애플 인증 서버 URL


    private final AppleClient appleClient;

    public AppleTokenResponse getAppleToken(String code) {
        // AppleTokenRequest 객체 생성
        AppleTokenRequest appleTokenRequest = AppleTokenRequest.builder()
                .clientId(clientId)  // 애플 클라이언트 ID
                .clientSecret(this.createClientSecret())  // JWT로 서명된 client_secret 생성
                .code(code)  // 애플 로그인에서 받은 인증 코드
                .grantType("authorization_code")  // 그랜트 타입 설정 (authorization_code)
                .build();

        // 로깅: client_secret 확인
        log.info("client_secret 생성 완료: {}", createClientSecret());  // client_secret 확인

        // 로깅: AppleTokenRequest 객체 정보 출력
        log.info("AppleTokenRequest: client_id={}, code={}, grant_type={}",
                appleTokenRequest.getClientId(),
                appleTokenRequest.getCode(),
                appleTokenRequest.getGrantType());

        // FeignClient를 사용하여 애플 토큰 발급 API 호출
        AppleTokenResponse tokenResponse = appleClient.findAppleToken(appleTokenRequest);

        // 로깅: 응답 객체 정보 확인
        log.info("AppleTokenResponse 수신: access_token={}, refresh_token={}",
                tokenResponse.accessToken(),
                tokenResponse.refreshToken());

        return tokenResponse;
    }


    // client_secret 생성 메소드 (JWT 서명)
    public String createClientSecret() {
        // 현재 시간에서 30일 동안 유효한 JWT 생성
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30)
                .atZone(ZoneId.systemDefault()).toInstant());

        try {
            // JWT 생성
            String jwt = Jwts.builder()
                    .setHeaderParam("kid", keyId)  // 헤더에 Key ID 추가
                    .setHeaderParam("alg", "ES256")  // 알고리즘 ES256 사용
                    .setIssuer(teamId)  // 발급자 (팀 ID)
                    .setIssuedAt(new Date(System.currentTimeMillis()))  // 발급 시간
                    .setExpiration(expirationDate)  // 만료 시간
                    .setAudience(authUrl)  // 애플의 인증 서버 URL (Audience)
                    .setSubject(clientId)  // 클라이언트 ID를 subject로 설정 (Bundle ID)
                    .signWith(getPrivateKey(), SignatureAlgorithm.ES256)  // 서명에 사용할 Private Key
                    .compact();  // JWT 생성 및 반환

            // 로깅: JWT 생성 완료
            log.info("JWT 생성 완료: {}", jwt);
            return jwt;
        } catch (Exception e) {
            // 로깅: JWT 생성 실패
            log.error("JWT 생성 실패: {}", e.getMessage(), e);
            throw new RuntimeException("Jwt 생성에 실패 했습니다", e);
        }
    }


    // private key를 불러오는 메소드
    private PrivateKey getPrivateKey() throws Exception {
        // PEM 형식의 개인 키
        String privateKeyPEM = "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgEedSzFzy5VAv8sSJxQPLbJHf2ufygFJfVe49CC0TSSOgCgYIKoZIzj0DAQehRANCAAQK4eM1rn+QA2gE/m7ZME91YR3o+lAV0SCJcR4wXCCtPhPZ2/npxdRCk7u3Iw+TSzahJez2quOhdHT/ukEvlbXO";

        // Base64로 디코딩
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);

        // PKCS8EncodedKeySpec으로 PrivateKey 객체로 변환
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");  // 애플은 Elliptic Curve 알고리즘 사용 (ES256)
        return keyFactory.generatePrivate(keySpec);
    }


}
