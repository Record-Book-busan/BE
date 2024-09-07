package busim.kkilogbu.user.controller;

import busim.kkilogbu.bookmark.dto.BookmarkResponse;
import busim.kkilogbu.record.dto.MyRecordResponse;
import busim.kkilogbu.user.appple.domain.dto.AppleSignInRequest;
import busim.kkilogbu.user.appple.domain.dto.SignInResponse;
import busim.kkilogbu.user.dto.RequestUserCategory;
import busim.kkilogbu.user.dto.RequestUserNickname;
import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.dto.UserInfoRequest;
import busim.kkilogbu.user.dto.UserInfoResponse;
import busim.kkilogbu.user.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/kkilogbu/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/signin/apple")
    public ResponseEntity<?> appleSignIn(@RequestBody AppleSignInRequest request) {
        try {

            String authorizationCode = request.getAuthorizationCode();
            String identityToken = request.getIdentityToken();

            SignInResponse result = userService.appleSignIn(authorizationCode, identityToken);
            return ResponseEntity.ok(result);

    } catch (IllegalArgumentException e) {
        // 잘못된 매개변수나 토큰이 유효하지 않은 경우
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다: " + e.getMessage());

    } catch (
    EntityNotFoundException e) {
        // 사용자를 찾을 수 없는 경우
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다: " + e.getMessage());

    } catch (HttpServerErrorException e) {
        // 애플 서버에서 문제가 발생한 경우
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("애플 서버에 문제가 발생했습니다: " + e.getMessage());

    } catch (Exception e) {
            // 그 외 예상하지 못한 예외
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다: " + e.getMessage());
        }
    }

        // 회원 탈퇴 API
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "userId") Long userId, @RequestParam String accessToken) {
        try {
            userService.deleteUserAccount(userId, accessToken);
            return ResponseEntity.ok("탈퇴 되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User deletion failed: " + e.getMessage());
        }
    }

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

//    /**
//     * 닉네임 변경, 프로필 사진 변경
//     */
//    @PostMapping("/info")
//    // TODO : 프로필 사진 변경
//    public ResponseEntity<?> changeUserInfo(@RequestBody UserInfoRequest request){
//        userService.changeUserInfo(request);
//        return ResponseEntity.ok().build();
//    }

    /**
     * 유저 카테고리 변경
     */
    @PostMapping("/category")
    public ResponseEntity<?> userCategory(@RequestBody(required = true) RequestUserCategory request){
        userService.changeCategory(request.getCategory());
        return ResponseEntity.ok().build();
    }
}
