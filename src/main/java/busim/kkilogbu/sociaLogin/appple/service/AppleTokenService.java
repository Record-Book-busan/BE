package busim.kkilogbu.sociaLogin.appple.service;


import busim.kkilogbu.sociaLogin.appple.controller.AppleClient;
import busim.kkilogbu.sociaLogin.appple.domain.dto.AppleKeyInfo;
import busim.kkilogbu.sociaLogin.appple.domain.dto.ApplePublicKeyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleTokenService {


    private final AppleClient appleClient;


    private final ObjectMapper objectMapper;

    public Claims verifyIdentityToken(String identityToken) {
        try {
            // 1. 애플 공개 키 가져오기
            log.info("애플 공개 키 요청 시작");
            ApplePublicKeyResponse response = appleClient.findAppleAuthPublicKeys();
            log.info("애플 공개 키 요청 완료: {}", response);

            // 2. JWT 헤더 부분 추출 및 디코딩
            log.info("JWT 헤더 추출 시작");
            String[] jwtParts = identityToken.split("\\.");
            String identityTokenHeader = jwtParts[0];
            String decodedIdentityTokenHeader = new String(Base64.getUrlDecoder().decode(identityTokenHeader), StandardCharsets.UTF_8);
            log.info("JWT 헤더 디코딩 완료: {}", decodedIdentityTokenHeader);

            // 3. JWT 헤더에서 kid, alg 추출
            log.info("JWT 헤더에서 kid, alg 추출 시작");
            Map<String, String> identityTokenHeaderMap = objectMapper.readValue(decodedIdentityTokenHeader, Map.class);
            String kid = identityTokenHeaderMap.get("kid");
            String alg = identityTokenHeaderMap.get("alg");
            log.info("JWT 헤더 정보 추출 완료 - kid: {}, alg: {}", kid, alg);

            // 4. kid, alg와 일치하는 공개 키 정보 찾기
            log.info("일치하는 공개 키 정보 찾기 시작");
            AppleKeyInfo appleKeyInfo = response.keys().stream()
                    .filter(key -> Objects.equals(key.getKid(), kid) && Objects.equals(key.getAlg(), alg))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 공개 키 정보 찾기 실패했습니다."));
            log.info("일치하는 공개 키 정보 찾기 성공: {}", appleKeyInfo);

            // 5. 공개 키 정보(n, e)를 사용해 RSA 공개 키 생성
            log.info("RSA 공개 키 생성 시작");
            byte[] nBytes = Base64.getUrlDecoder().decode(appleKeyInfo.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(appleKeyInfo.getE());
            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(appleKeyInfo.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            log.info("RSA 공개 키 생성 완료");

            // 6. 공개 키로 JWT 서명 검증 및 클레임 반환
            log.info("JWT 서명 검증 시작");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(identityToken)
                    .getBody();
            log.info("JWT 서명 검증 성공 - 클레임: {}", claims);
            return claims;

        } catch (IllegalArgumentException e) {
            log.error("일치하는 공개 키 정보 찾기 실패: {}", e.getMessage(), e);
            throw new IllegalArgumentException("서명 검증 및 클레임 반환 실패: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("서명 검증 및 클레임 반환 중 오류 발생: {}", e.getMessage(), e);
            throw new IllegalArgumentException("서명 검증 및 클레임 반환 실패: " + e.getMessage(), e);
        }
    }
}
