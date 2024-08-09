package busim.kkilogbu.user.controller;

import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userInfo")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<Void> userInfo(@RequestBody UserDto user){
        userService.userInfo(user);
        return new ResponseEntity(userService.userInfo(user), HttpStatus.OK);
    }
}
