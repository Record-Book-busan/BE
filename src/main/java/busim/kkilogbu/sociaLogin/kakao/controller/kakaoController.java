package busim.kkilogbu.sociaLogin.kakao.controller;


import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;
import busim.kkilogbu.sociaLogin.kakao.serivce.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/kkilogbu/users")
@RequiredArgsConstructor
public class kakaoController{

    private final KakaoService kakaoService;

    @PostMapping("/signin/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam(name = "accessToken") String accessToken) {
        try {

            log.info("카카오 로그인 시작 하기 : " + accessToken);
            // KakaoService를 호출하여 로그인 처리 및 JWT 생성
            SignInResponse response = kakaoService.processKakaoLogin(accessToken);
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
