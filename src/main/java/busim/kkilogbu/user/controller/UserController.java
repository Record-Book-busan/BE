package busim.kkilogbu.user.controller;


import java.util.List;

import busim.kkilogbu.bookmark.dto.BookmarkResponse;
import busim.kkilogbu.security.util.JwtUtil;
import busim.kkilogbu.sociaLogin.appple.domain.dto.*;
import busim.kkilogbu.sociaLogin.appple.service.AppleLoginService;

import busim.kkilogbu.user.dto.UserDto;

import busim.kkilogbu.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;



@RestController
@RequestMapping("/kkilogbu/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AppleLoginService appleLoginService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signin/apple")
    public ResponseEntity<?> socialSignIn(@RequestBody AppleSignInRequest request) {
        try {
            String authorizationCode = request.getAuthorizationCode();
            String identityToken = request.getIdentityToken();

            var result = appleLoginService.signInOrRegister(authorizationCode, identityToken);
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("애플 서버에 문제가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/signin/anonymous")
    public ResponseEntity<String> guestSignIn() {
        try {

            // 비회원용 JWT 생성
            String guestToken = jwtUtil.createGuestToken();

            // 게스트 사용자의 정보를 저장할 필요가 없다면, 게스트용 JWT만 발급하여 응답
            return ResponseEntity.ok("Bearer " + guestToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("비회원 로그인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/signin/{userId}/consent")
    public ResponseEntity<String> saveUserConsent(
            @PathVariable(name = "userId") Long userId,
            @RequestBody UserConsentRequest consentRequest) {

        userService.saveUserConsent(userId, consentRequest);
        return ResponseEntity.ok("개인정보 와 약관동의 하셨습니다");
    }



        // 회원 탈퇴 API
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "userId") Long userId, @RequestParam String accessToken) {
        try {
            userService.deleteUserAccount(userId, accessToken);
            return ResponseEntity.ok("탈퇴 되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Users deletion failed: " + e.getMessage());
        }
    }




    /**
     * 유저 정보 조회
     */
//    @GetMapping
//    public ResponseEntity<UserInfoResponse> getUserInfo(){
//        return ResponseEntity.ok(userService.getUserInfo());
//    }

//    /**
//     * 내 기록 조회
//     */
//    @GetMapping("/record")
//    public ResponseEntity<Slice<MyRecordResponse>> getMyRecord(@PageableDefault(size = 10, page = 0) Pageable pageable){
//        return ResponseEntity.ok(userService.getMyRecord(pageable));
//    }
//
//

    @Operation(
            summary = "북마크 조회",
            description = "북마크한 기록을 조회합니다."
    )
    @GetMapping("/bookmark/record")
    public ResponseEntity<List<BookmarkResponse>> getBookmark(){
        return ResponseEntity.ok(userService.getBookmark());
    }

    @PostMapping
    public ResponseEntity<?> userInfo(@RequestBody UserDto user){
        return ResponseEntity.ok(userService.userInfo(user));
    }

//    /**
//     * 닉네임 중복 체크
//     */
//    @PostMapping("/name/check")
//    public ResponseEntity<Boolean> checkNicknameDuplicate(@Valid @RequestBody(required = true) RequestUserNickname request){
//        return ResponseEntity.ok(userService.checkUsernameDuplicate(request.getNickName()));
//    }

//    /**
//     * 닉네임 변경, 프로필 사진 변경
//     */
//    @PostMapping("/info")
//    // TODO : 프로필 사진 변경
//    public ResponseEntity<?> changeUserInfo(@RequestBody UserInfoRequest request){
//        userService.changeUserInfo(request);
//        return ResponseEntity.ok().build();
//    }

//    /**
//     * 유저 카테고리 변경
//     */
//    @PostMapping("/category")
//    public ResponseEntity<?> userCategory(@RequestBody(required = true) RequestUserCategory request){
//        userService.changeCategory(request.getCategory());
//        return ResponseEntity.ok().build();
//    }
}
