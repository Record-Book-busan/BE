package busim.kkilogbu.user.appple.service;


import busim.kkilogbu.user.appple.controller.AppleClient;
import busim.kkilogbu.user.appple.domain.dto.AppleKeyInfo;
import busim.kkilogbu.user.appple.domain.dto.ApplePublicKeyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppleTokenService {


    private final AppleClient appleClient;


    private final ObjectMapper objectMapper;

    public Claims verifyIdentityToken(String identityToken) {
        try {
            // 1. 애플 공개 키 가져오기
            ApplePublicKeyResponse response = appleClient.findAppleAuthPublicKeys();

            // 2. JWT 헤더 부분 추출 및 디코딩
            String[] jwtParts = identityToken.split("\\.");
            String identityTokenHeader = jwtParts[0];
            String decodedIdentityTokenHeader = new String(Base64.getUrlDecoder().decode(identityTokenHeader), StandardCharsets.UTF_8);

            // 3. JWT 헤더에서 kid, alg 추출
            Map<String, String> identityTokenHeaderMap = objectMapper.readValue(decodedIdentityTokenHeader, Map.class);
            String kid = identityTokenHeaderMap.get("kid");
            String alg = identityTokenHeaderMap.get("alg");

            // 4. kid, alg와 일치하는 공개 키 정보 찾기
            AppleKeyInfo appleKeyInfo = response.keys().stream()
                    .filter(key -> Objects.equals(key.getKid(), kid) && Objects.equals(key.getAlg(), alg))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 공개 키 정보 찾기 실패했습니다. "));

            // 5. 공개 키 정보(n, e)를 사용해 RSA 공개 키 생성
            byte[] nBytes = Base64.getUrlDecoder().decode(appleKeyInfo.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(appleKeyInfo.getE());
            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(appleKeyInfo.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // 6. 공개 키로 JWT 서명 검증 및 클레임 반환
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(identityToken)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("서명 검증 및 클레임 반환 실패 했습니다");
        }
    }
}