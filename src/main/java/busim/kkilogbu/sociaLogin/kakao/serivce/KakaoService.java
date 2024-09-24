package busim.kkilogbu.sociaLogin.kakao.serivce;


import busim.kkilogbu.security.util.JwtUtil;
import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;


import busim.kkilogbu.sociaLogin.kakao.dto.KakaoTokenDto;
import busim.kkilogbu.user.dto.SignInResponseMapper;
import busim.kkilogbu.user.entity.users.Users;
import busim.kkilogbu.user.repository.UserRepository;
import busim.kkilogbu.user.service.UserService;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {


    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    //    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final RestTemplate restTemplate = new RestTemplate();
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://kapi.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
            .build();


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
//            Users users = userRepository.findBySocialUserId(kakao_sub)
//                    .orElseThrow(() -> new IllegalArgumentException("카카오 계정으로 이미 등록된 사용자가 없습니다"));
//
//
//            // Access Token 및 Refresh Token 생성
//            String accessToken = jwtUtil.createAccessToken(kakao_sub);  // Access Token 생성
//            String refreshToken = jwtUtil.createRefreshToken(kakao_sub);  // Refresh Token 생성
//
//            // 사용자 정보 업데이트 (로그인 타입과 역할 포함)
//            users.kakaoSignin(kakao_sub, email, username, picture);
//            users.updateTokens(accessToken, refreshToken);
//
//            userRepository.save(users);
//
//            // SignInResponseMapper를 사용해 SignInResponse 생성 및 반환
//            return SignInResponseMapper.toSignInResponse(users, accessToken, refreshToken);
//        }


    // 사용자 정보 조회
    public KakaoTokenDto getUserInfo(String accessToken) {

        KakaoTokenDto userInfo = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/user/me")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("카카오 사용자 정보 요청 중 4xx 클라이언트 오류 발생");
                    return Mono.error(new IllegalArgumentException("잘못된 카카오 Access Token입니다."));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("카카오 사용자 정보 요청 중 5xx 서버 오류 발생");
                    return Mono.error(new IllegalArgumentException("카카오 서버 오류가 발생했습니다."));
                })
                .bodyToMono(KakaoTokenDto.class)
                .block();

        if (userInfo == null || userInfo.getId() == null) {
            log.error("카카오 사용자 정보 조회 실패: 사용자 정보가 없습니다.");
            throw new RuntimeException("카카오 사용자 정보를 찾을 수 없습니다.");
        }

        log.info("[카카오 서비스] 사용자 ID: {}", userInfo.getId());
        log.info("[카카오 서비스] 닉네임: {}", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[카카오 서비스] 프로필 이미지 URL: {}", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }

    public SignInResponse processKakaoLogin(String accessToken) {
        // Kakao에서 사용자 정보 가져오기
        KakaoTokenDto kakaoUserInfo = getUserInfo(accessToken);
        String kakaoSub = String.valueOf(kakaoUserInfo.getId());  // 카카오 사용자 고유 ID
        String email = kakaoUserInfo.getKakaoAccount().getEmail();
        String nickname = kakaoUserInfo.getKakaoAccount().getProfile().getNickName();
        String picture = kakaoUserInfo.getKakaoAccount().getProfile().getProfileImageUrl();

        // Access Token 및 Refresh Token 생성
        String newAccessToken = jwtUtil.createAccessToken(kakaoSub);
        String newRefreshToken = jwtUtil.createRefreshToken(kakaoSub);
        log.info("JWT 생성 완료: AccessToken={}, RefreshToken={}", newAccessToken, newRefreshToken);

        // 카카오 계정으로 등록된 사용자 찾기
        Users users = userRepository.findBySocialUserId(kakaoSub).orElse(null);

        if (users == null) {
            // 신규 사용자일 경우 회원가입 진행
            log.info("새로운 카카오 사용자 발견. 회원가입 진행 중: 카카오 ID={}", kakaoSub);
            users = registerNewUser(kakaoSub, email, nickname, picture, newAccessToken, newRefreshToken);
            log.info("새로운 사용자 저장 완료: 사용자 ID={}", users.getId());
        } else {
            // 기존 사용자일 경우 사용자 정보 및 토큰 업데이트
            log.info("기존 카카오 사용자 정보 업데이트 진행 중: 사용자 ID={}", users.getId());
            updateUserTokens(users, newAccessToken, newRefreshToken);
        }

        // SignInResponseMapper를 사용해 SignInResponse 생성 및 반환
        SignInResponse signInResponse = SignInResponseMapper.toSignInResponse(users, newAccessToken, newRefreshToken);
        log.info("로그인 응답 반환 완료: 사용자 ID={}", users.getId());

        return signInResponse;
    }

    // 신규 사용자 등록 메서드
    private Users registerNewUser(String kakaoSub, String email, String nickname, String picture, String accessToken, String refreshToken) {
        Users newUsers = Users.builder()
                .socialUserId(kakaoSub)
                .loginType("KAKKO")
                .email(email)
                .nickname(nickname)
                .profileImage(picture)
                .role("USER")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return userRepository.save(newUsers);
    }

    // 기존 사용자의 토큰 업데이트 메서드
    private void updateUserTokens(Users users, String accessToken, String refreshToken) {
        users.updateTokens(accessToken, refreshToken);
        userRepository.save(users);
    }
}