package busim.kkilogbu.user.controller;

import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUserInfo(){
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PostMapping
    public ResponseEntity<Void> userInfo(@RequestBody UserDto user){
        userService.userInfo(user);
        return new ResponseEntity(userService.userInfo(user), HttpStatus.OK);
    }

    @PostMapping("/name/check")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestBody(required = true) String name){
        return ResponseEntity.ok(userService.checkUsernameDuplicate(name));
    }
    @PostMapping("/name")
    public ResponseEntity<?> changeNickname(@RequestBody(required = true) String name){
        userService.changeUsername(name);
        return ResponseEntity.ok().build();
    }
}
