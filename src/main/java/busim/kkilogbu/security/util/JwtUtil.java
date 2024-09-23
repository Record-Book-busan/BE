package busim.kkilogbu.security.util;

import busim.kkilogbu.user.entity.users.Users;
import busim.kkilogbu.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

    private final UserRepository userRepository;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    // Access Token 만료 시간 (1시간)
    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60;

    private final long GUEST_ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 20;

    // Refresh Token 만료 시간 (20일)
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 20;

    // RSA 비대칭키 생성자
    public JwtUtil(UserRepository userRepository) throws Exception {
        this.userRepository = userRepository;

        // RSA 키쌍 생성
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // RSA 2048 비트 키 쌍 생성
        KeyPair keyPair = keyGen.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    // 회원용 Access Token 생성 (RSA 서명)
    public String createAccessToken(String socialUserId) {
        return Jwts.builder()
                .setSubject(socialUserId)  // userId를 subject로 설정
                .claim("role", "USER")  // 역할을 USER로 설정
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))  // 만료 시간
                .signWith(privateKey, SignatureAlgorithm.RS256)  // RSA-SHA256 서명
                .compact();
    }

    // 비회원(게스트)용 Access Token 생성 (RSA 서명)
    public String createGuestToken() {
        String guestId = UUID.randomUUID().toString();  // 게스트 사용자용 UUID 생성
        return Jwts.builder()
                .setSubject(guestId)  // guestId를 subject로 설정
                .claim("role", "GUEST")  // 역할을 GUEST로 설정
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + GUEST_ACCESS_TOKEN_EXPIRATION))  // 만료 시간
                .signWith(privateKey, SignatureAlgorithm.RS256)  // RSA-SHA256 서명
                .compact();
    }


    // Refresh Token 생성 (RSA 서명)
    public String createRefreshToken(String socialUserId) {
        return Jwts.builder()
                .setSubject(socialUserId)
                .claim("role", "REFRESH")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))  // 만료 시간
                .signWith(privateKey, SignatureAlgorithm.RS256)  // RSA-SHA256 서명
                .compact();
    }

    // JWT에서 사용자 이름 또는 guestId 추출
    public String extractEmail(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));

        if ("GUEST".equals(role)) {
            return extractClaim(token, Claims::getSubject);  // guestId 반환
        } else if ("USER".equals(role)) {
            String socialUserId = extractClaim(token, Claims::getSubject);

            // userId로 데이터베이스에서 사용자 조회
            Users users = userRepository.findBySocialUserId(socialUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + socialUserId));

            return users.getSocialUserId();  // 이메일 반환
        }
        throw new IllegalStateException("알 수 없는 사용자 유형");
    }
    public String extractRole(String token) {
        try {
            // JWT에서 'role' 클레임을 추출
            String role = extractClaim(token, claims -> claims.get("role", String.class));

            if ("GUEST".equals(role)) {
                // GUEST 역할일 경우, 추가 처리 로직이 필요하다면 여기에 작성
                log.info("GUEST 사용자 역할 확인됨.");
                return "GUEST";  // GUEST일 때 처리
            } else if ("USER".equals(role)) {
                // USER 역할일 경우, 일반적인 처리
                log.info("USER 역할 확인됨.");
                return "USER";
            } else {
                // 기타 알려지지 않은 역할에 대한 처리
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

    // 모든 클레임 추출 (RSA 공개키로 서명 검증)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)  // 공개키로 서명 검증
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
