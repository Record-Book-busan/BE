package busim.kkilogbu.sociaLogin.kakao.serivce;


import busim.kkilogbu.security.util.JwtUtil;
import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;
import busim.kkilogbu.sociaLogin.kakao.dto.KakaoTokenDto;
import busim.kkilogbu.user.dto.SignInResponseMapper;

import busim.kkilogbu.user.entity.users.User;
import busim.kkilogbu.user.repository.UserRepository;
import busim.kkilogbu.user.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {


    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

//        // 카카오 소셜 로그인 메서드
//        public SignInResponse login(String jsonString) throws Exception {
//            // JSON 문자열을 파싱하여 JsonNode로 변환
//            JsonNode kakao_parse = objectMapper.readTree(jsonString);  // JSON 파싱
//
//            // 카카오 API에서 받은 사용자 정보와 토큰 파싱
//            JsonNode kakao_profile = kakao_parse.get("kakaoProfileResult");
//            JsonNode kakao_token = kakao_parse.get("accessToken");
//
//            // 사용자 정보 추출
//            String kakao_sub = kakao_profile.get("id").asText(); // 카카오 사용자 고유 ID
//            String email = kakao_profile.get("email").asText();
//            String username = kakao_profile.get("nickname").asText();
//            String picture = kakao_profile.get("profileImageUrl").asText();
//
//            // 사용자 정보 저장 또는 업데이트
//            User user = userRepository.findBySocialUserId(kakao_sub)
//                    .orElseThrow(() -> new IllegalArgumentException("카카오 계정으로 이미 등록된 사용자가 없습니다"));
//
//
//            // Access Token 및 Refresh Token 생성
//            String accessToken = jwtUtil.createAccessToken(kakao_sub);  // Access Token 생성
//            String refreshToken = jwtUtil.createRefreshToken(kakao_sub);  // Refresh Token 생성
//
//            // 사용자 정보 업데이트 (로그인 타입과 역할 포함)
//            user.kakaoSignin(kakao_sub, email, username, picture);
//            user.updateTokens(accessToken, refreshToken);
//
//            userRepository.save(user);
//
//            // SignInResponseMapper를 사용해 SignInResponse 생성 및 반환
//            return SignInResponseMapper.toSignInResponse(user, accessToken, refreshToken);
//        }


    // 카카오 소셜 로그인 메서드
    public SignInResponse login(KakaoTokenDto jsonString) throws Exception {
        log.info("카카오 로그인 처리 시작");

        // JSON 데이터를 파싱하여 accessToken을 추출
        String accessToken = jsonString.getAccessToken();
        log.info("클라이언트로부터 받은 accessToken: {}", accessToken);

        // 카카오 사용자 정보 API 호출 (accessToken 사용)
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        log.info("카카오 사용자 정보 API 호출 시작");
        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);
        log.info("카카오 사용자 정보 API 응답 수신 완료");

        JsonNode kakaoProfileData = objectMapper.readTree(response.getBody());
        log.info("카카오 프로필 데이터 파싱 완료: {}", kakaoProfileData.toString());

        // 사용자 정보 추출
        String kakaoSub = kakaoProfileData.get("id").asText();  // 카카오 사용자 고유 ID
        String email = kakaoProfileData.path("kakao_account").path("email").asText(null);
        String nickname = kakaoProfileData.path("properties").path("nickname").asText();
        String picture = kakaoProfileData.path("properties").path("profile_image").asText(null);

        log.info("카카오 사용자 정보 추출 완료: kakaoSub={}, email={}, nickname={}", kakaoSub, email, nickname);

        // 이미 등록된 사용자 확인 (없으면 예외 발생)
        User user = userRepository.findBySocialUserId(kakaoSub)
                .orElseThrow(() -> new IllegalArgumentException("카카오 계정으로 이미 등록된 사용자가 없습니다"));

        log.info("기존 카카오 사용자 정보 업데이트 진행 중: userId={}", user.getId());

        // Access Token 및 Refresh Token 생성
        String newAccessToken = jwtUtil.createAccessToken(kakaoSub);  // Access Token 생성
        String newRefreshToken = jwtUtil.createRefreshToken(kakaoSub);  // Refresh Token 생성

        log.info("JWT 생성 완료: newAccessToken={}, newRefreshToken={}", newAccessToken, newRefreshToken);

        // 사용자 정보 업데이트 및 저장
        user.kakaoSignin(kakaoSub, email, nickname, picture);
        user.updateTokens(newAccessToken, newRefreshToken);
        userRepository.save(user);

        log.info("사용자 정보 및 토큰 저장 완료: userId={}", user.getId());

        // SignInResponseMapper를 사용해 SignInResponse 생성 및 반환
        SignInResponse signInResponse = SignInResponseMapper.toSignInResponse(user, newAccessToken, newRefreshToken);
        log.info("로그인 응답 반환 완료: userId={}", user.getId());

        return signInResponse;
    }

}

