package busim.kkilogbu.user.controller;


import java.util.List;

import busim.kkilogbu.bookmark.dto.BookmarkResponse;
import busim.kkilogbu.record.dto.MyRecordResponse;
import busim.kkilogbu.security.util.JwtUtil;
import busim.kkilogbu.sociaLogin.appple.domain.dto.*;
import busim.kkilogbu.sociaLogin.appple.service.AppleLoginService;

import busim.kkilogbu.user.dto.RequestUserNickname;
import busim.kkilogbu.user.dto.UserDto;

import busim.kkilogbu.user.dto.UserImageUpdateRequest;
import busim.kkilogbu.user.dto.UserNameUpdateRequest;
import busim.kkilogbu.user.dto.UserInfoResponse;
import busim.kkilogbu.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.hibernate.query.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PostMapping("/signin/consent")
    public ResponseEntity<String> saveUserConsent(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserConsentRequest consentRequest) {
        String consent = userService.saveUserConsent(userDetails.getUsername(), consentRequest);
        return ResponseEntity.ok(consent);
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



    @Operation(
            summary = "회원 정보 조회",
            description = "회원의 정보를 조회합니다."
    )
   @GetMapping
   public ResponseEntity<UserInfoResponse> getUserInfo(){
       return ResponseEntity.ok(userService.getUserInfo());
   }

   /**
    * 내 기록 조회
    */
   @GetMapping("/record")
   public ResponseEntity<List<MyRecordResponse>> getMyRecord(
       @Parameter(description = "데이터의 시작점", example = "0") @RequestParam(defaultValue = "0") int offset,
       @Parameter(description = "한 번에 가져올 데이터 수", example = "9") @RequestParam(defaultValue = "9") int limit
   ){
       return ResponseEntity.ok(userService.getMyRecord(offset, limit));
   }

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

   @PostMapping("/name/check")
   public ResponseEntity<Boolean> checkNicknameDuplicate(@Valid @RequestBody(required = true) RequestUserNickname request){
       return ResponseEntity.ok(userService.checkUsernameDuplicate(request.getNickName()));
   }

   @PostMapping("/name")
   public ResponseEntity<?> changeUserImage(@RequestBody(required = true) UserNameUpdateRequest request){
       userService.changeUserImage(request.getNickName());
       return ResponseEntity.ok().build();
   }

   @PostMapping("/image")
    public ResponseEntity<?> changeUserNickname(@RequestBody(required = true) UserImageUpdateRequest request){
        userService.changeUserNickname(request.getProfileImage());
        return ResponseEntity.ok().build();
    }

//    /**
//     * 유저 카테고리 변경
//     */
//    @PostMapping("/category")
//    public ResponseEntity<?> userCategory(@RequestBody(required = true) RequestUserCategory request){
//        userService.changeCategory(request.getCategory());
//        return ResponseEntity.ok().build();
//    }
}
