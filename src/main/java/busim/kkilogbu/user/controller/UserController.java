package busim.kkilogbu.user.controller;

import java.util.List;

import busim.kkilogbu.bookmark.dto.BookmarkResponse;
import busim.kkilogbu.record.dto.MyRecordResponse;
import busim.kkilogbu.record.service.RecordService;
import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.dto.UserInfoResponse;
import busim.kkilogbu.user.service.UserService;
import lombok.RequiredArgsConstructor;

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
    private final RecordService recordService;

    /**
     * 유저 정보 조회
     */
    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(){
        return ResponseEntity.ok(userService.getUserInfo());
    }

    /**
     * 내 기록 조회
     */
    @GetMapping("/myRecord")
    // TODO : pagination 적용?
    public ResponseEntity<List<MyRecordResponse>> getMyRecord(){
        return ResponseEntity.ok(userService.getMyRecord());
    }

    /**
     * 북마크 조회
     */
    @GetMapping("/bookmark")
    // TODO : pagination 적용?
    public ResponseEntity<List<BookmarkResponse>> getBookmark(){
        return ResponseEntity.ok(userService.getBookmark());
    }

    @PostMapping
    public ResponseEntity<?> userInfo(@RequestBody UserDto user){
        return ResponseEntity.ok(userService.userInfo(user));
    }

    /**
     * 닉네임 중복 체크
     */
    @PostMapping("/name/check")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestBody(required = true) String name){
        return ResponseEntity.ok(userService.checkUsernameDuplicate(name));
    }

    /**
     * 닉네임 변경, 프로필 사진 변경
     */
    @PostMapping("/name")
    // TODO : 프로필 사진 변경
    public ResponseEntity<?> changeNickname(@RequestBody(required = true) String name){
        userService.changeUsername(name);
        return ResponseEntity.ok().build();
    }

    /**
     * 유저 카테고리 변경
     */
    @PostMapping("/category")
    public ResponseEntity<?> userCategory(@RequestBody(required = true) Long category){
        userService.changeCategory(category);
        return ResponseEntity.ok().build();
    }
}
