package busim.kkilogbu.user.controller;
import busim.kkilogbu.security.util.JwtUtil;
import busim.kkilogbu.user.dto.RefreshTokenRequest;
import busim.kkilogbu.user.dto.RefreshTokenResponse;
import busim.kkilogbu.user.entity.users.User;
import busim.kkilogbu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // Refresh Token을 사용해 Access Token 갱신하는 API
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        try {
            // Refresh Token 유효성 검증
            if (jwtUtil.validateToken(refreshToken, true)) {
                // Refresh Token이 유효하다면, userId를 추출
                String userEmail = jwtUtil.extractEmail(refreshToken);

                User user = userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userEmail));

                // 새로운 Access Token 생성 (사용자의 ID 사용)
                String newAccessToken = jwtUtil.createAccessToken(String.valueOf(user.getId()));
                // 응답으로 새로운 Access Token을 반환
                return ResponseEntity.ok(new RefreshTokenResponse("Bearer " + newAccessToken, refreshToken));
            } else {
                // Refresh Token이 유효하지 않으면 401 Unauthorized 응답
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token이 유효하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다: " + e.getMessage());
        }
    }
}
