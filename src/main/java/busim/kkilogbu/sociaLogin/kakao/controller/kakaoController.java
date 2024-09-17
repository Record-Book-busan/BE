package busim.kkilogbu.sociaLogin.kakao.controller;


import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;
import busim.kkilogbu.sociaLogin.kakao.dto.KakaoTokenDto;
import busim.kkilogbu.sociaLogin.kakao.serivce.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/kkilogbu/user")
@RequiredArgsConstructor
public class kakaoController{

    private final KakaoService kakaoService;

    @PostMapping("/signin/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoTokenDto jsonString) {
        try {

            log.info("카카오 로그인 시작 하기 : " + jsonString.getAccessToken());
            // KakaoService를 호출하여 로그인 처리 및 JWT 생성
            SignInResponse response = kakaoService.login(jsonString);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 잘못된 사용자 정보가 입력되었을 때 예외 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // 기타 예외 발생 시 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 처리 중 오류가 발생했습니다.");
        }
    }
}
