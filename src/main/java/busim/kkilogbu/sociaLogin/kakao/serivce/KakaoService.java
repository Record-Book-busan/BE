package busim.kkilogbu.sociaLogin.kakao.serivce;


import busim.kkilogbu.security.util.JwtUtil;
import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;
import busim.kkilogbu.user.dto.SignInResponseMapper;

import busim.kkilogbu.user.entity.users.User;
import busim.kkilogbu.user.repository.UserRepository;
import busim.kkilogbu.user.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoService {


    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();  // Jackson ObjectMapper 인스턴스

        // 카카오 소셜 로그인 메서드
        public SignInResponse login(String jsonString) throws Exception {
            // JSON 문자열을 파싱하여 JsonNode로 변환
            JsonNode kakao_parse = objectMapper.readTree(jsonString);  // JSON 파싱

            // 카카오 API에서 받은 사용자 정보와 토큰 파싱
            JsonNode kakao_profile = kakao_parse.get("kakaoProfileResult");
            JsonNode kakao_token = kakao_parse.get("accessToken");

            // 사용자 정보 추출
            String kakao_sub = kakao_profile.get("id").asText(); // 카카오 사용자 고유 ID
            String email = kakao_profile.get("email").asText();
            String username = kakao_profile.get("nickname").asText();
            String picture = kakao_profile.get("profileImageUrl").asText();

            // 사용자 정보 저장 또는 업데이트
            User user = userRepository.findBySocialUserId(kakao_sub)
                    .orElseThrow(() -> new IllegalArgumentException("카카오 계정으로 이미 등록된 사용자가 없습니다"));


            // Access Token 및 Refresh Token 생성
            String accessToken = jwtUtil.createAccessToken(kakao_sub);  // Access Token 생성
            String refreshToken = jwtUtil.createRefreshToken(kakao_sub);  // Refresh Token 생성

            // 사용자 정보 업데이트 (로그인 타입과 역할 포함)
            user.kakaoSignin(kakao_sub, email, username, picture);
            user.updateTokens(accessToken, refreshToken);

            userRepository.save(user);

            // SignInResponseMapper를 사용해 SignInResponse 생성 및 반환
            return SignInResponseMapper.toSignInResponse(user, accessToken, refreshToken);
        }
}

