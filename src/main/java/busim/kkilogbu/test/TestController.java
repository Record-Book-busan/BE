package busim.kkilogbu.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {


    @GetMapping("/test")
    public ResponseEntity<String> mainTest(){
        log.info("test 서버에 잘 들어왔습니다. 서버요청 확인 하곳 싶을 때 요청 주세요");
        log.info("화이팅 ~~~!!");
        return ResponseEntity.ok("로그인 없이 확이하려고 합니다");
    }
}
