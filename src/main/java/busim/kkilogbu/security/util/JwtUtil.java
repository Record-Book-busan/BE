package busim.kkilogbu.security.util;

import busim.kkilogbu.user.entity.users.Users;
import busim.kkilogbu.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtUtil {

    private final UserRepository userRepository;
    private final SecretKey secretKey;

    // Access Token 만료 시간 (180 일)
    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 180;

    // 비회원(게스트)용 Access Token 만료 시간 (100일)
    private final long GUEST_ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 100;

    // Refresh Token 만료 시간 (200일)
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 200;

    // HMAC 대칭키 생성자
    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;

        // 강력한 비밀 키 설정 (알파벳 대소문자, 숫자, 특수문자 포함)
        String secret = "KJsd89sd!kd2#9jsd@sdklfSDF*832jsdJ2kds";  // 비밀 키는 반드시 안전하게 관리되어야 함
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    // 회원용 Access Token 생성 (HMAC 서명)
    public String createAccessToken(String socialUserId) {
        return Jwts.builder()
                .setSubject(socialUserId)  // userId를 subject로 설정
                .claim("role", "USER")  // 역할을 USER로 설정
                .setIssuedAt(Date.from(Instant.now()))  // 현재 UTC 시간으로 발급 시간 설정
                .setExpiration(Date.from(Instant.now().plusMillis(ACCESS_TOKEN_EXPIRATION)))  // UTC 시간으로 만료 시간 설정
                .signWith(secretKey, SignatureAlgorithm.HS256)  // HMAC-SHA256 서명
                .compact();
    }

    // 비회원(게스트)용 Access Token 생성 (HMAC 서명)
    public String createGuestToken() {
        String guestId = UUID.randomUUID().toString();  // 게스트 사용자용 UUID 생성
        return Jwts.builder()
                .setSubject(guestId)  // guestId를 subject로 설정
                .claim("role", "GUEST")  // 역할을 GUEST로 설정
                .setIssuedAt(Date.from(Instant.now()))  // 현재 UTC 시간으로 발급 시간 설정
                .setExpiration(Date.from(Instant.now().plusMillis(GUEST_ACCESS_TOKEN_EXPIRATION)))  // UTC 시간으로 만료 시간 설정
                .signWith(secretKey, SignatureAlgorithm.HS256)  // HMAC-SHA256 서명
                .compact();
    }

    // Refresh Token 생성 (HMAC 서명)
    public String createRefreshToken(String socialUserId) {
        return Jwts.builder()
                .setSubject(socialUserId)
                .claim("role", "REFRESH")
                .setIssuedAt(Date.from(Instant.now()))  // 현재 UTC 시간으로 발급 시간 설정
                .setExpiration(Date.from(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION)))  // UTC 시간으로 만료 시간 설정
                .signWith(secretKey, SignatureAlgorithm.HS256)  // HMAC-SHA256 서명
                .compact();
    }

    // JWT에서 사용자 이름 또는 guestId 추출
    public String extractId(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));

        if ("GUEST".equals(role)) {
            return extractClaim(token, Claims::getSubject);  // guestId 반환
        } else if ("USER".equals(role)) {
            String socialUserId = extractClaim(token, Claims::getSubject);

            // userId로 데이터베이스에서 사용자 조회
            Users users = userRepository.findBySocialUserId(socialUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + socialUserId));

            return users.getSocialUserId();
        }
        throw new IllegalStateException("알 수 없는 사용자 유형");
    }

    // JWT에서 역할(role) 클레임 추출
    public String extractRole(String token) {
        try {
            String role = extractClaim(token, claims -> claims.get("role", String.class));

            if ("GUEST".equals(role)) {
                log.info("GUEST 사용자 역할 확인됨.");
                return "GUEST";
            } else if ("USER".equals(role)) {
                log.info("USER 역할 확인됨.");
                return "USER";
            } else {
                log.warn("알 수 없는 사용자 역할: {}", role);
                throw new IllegalStateException("알 수 없는 역할이 발견되었습니다: " + role);
            }
        } catch (Exception e) {
            log.error("역할 추출 중 오류 발생: {}", e.getMessage());
            throw new IllegalArgumentException("역할을 추출할 수 없습니다.", e);
        }
    }

    // 클레임 추출
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 모든 클레임 추출 (HMAC 서명 검증)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)  // 비밀키로 서명 검증
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검증 (Access Token, Refresh Token 구분)
    public boolean validateToken(String token, boolean isRefreshToken) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));

        if (isRefreshToken && "REFRESH".equals(role)) {
            return !isTokenExpired(token);
        } else if (!isRefreshToken && ("USER".equals(role) || "GUEST".equals(role))) {
            return !isTokenExpired(token);
        }
        return false;
    }

    // 토큰 만료 여부 확인
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰 만료 시간 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
