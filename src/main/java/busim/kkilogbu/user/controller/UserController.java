package busim.kkilogbu.user.controller;

import busim.kkilogbu.bookmark.dto.BookmarkResponse;
import busim.kkilogbu.record.dto.MyRecordResponse;
import busim.kkilogbu.user.dto.RequestUserCategory;
import busim.kkilogbu.user.dto.RequestUserNickname;
import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.dto.UserInfoRequest;
import busim.kkilogbu.user.dto.UserInfoResponse;
import busim.kkilogbu.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
    @GetMapping("/record")
    public ResponseEntity<Slice<MyRecordResponse>> getMyRecord(@PageableDefault(size = 10, page = 0) Pageable pageable){
        return ResponseEntity.ok(userService.getMyRecord(pageable));
    }

    /**
     * 북마크 조회
     */
    @GetMapping("/bookmark")
    public ResponseEntity<Slice<BookmarkResponse>> getBookmark(@PageableDefault(size = 10, page = 0) Pageable pageable, @PathParam("type") String type){
        return ResponseEntity.ok(userService.getBookmark(pageable, type));
    }

    @PostMapping
    public ResponseEntity<?> userInfo(@RequestBody UserDto user){
        return ResponseEntity.ok(userService.userInfo(user));
    }

    /**
     * 닉네임 중복 체크
     */
    @PostMapping("/name/check")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@Valid @RequestBody(required = true) RequestUserNickname request){
        return ResponseEntity.ok(userService.checkUsernameDuplicate(request.getNickName()));
    }

    /**
     * 닉네임 변경, 프로필 사진 변경
     */
    @PostMapping("/info")
    // TODO : 프로필 사진 변경
    public ResponseEntity<?> changeUserInfo(@RequestBody UserInfoRequest request){
        userService.changeUserInfo(request);
        return ResponseEntity.ok().build();
    }

    /**
     * 유저 카테고리 변경
     */
    @PostMapping("/category")
    public ResponseEntity<?> userCategory(@RequestBody(required = true) RequestUserCategory request){
        userService.changeCategory(request.getCategory());
        return ResponseEntity.ok().build();
    }
}
