package busim.kkilogbu.user.controller;

import busim.kkilogbu.bookmark.dto.BookmarkResponse;
import busim.kkilogbu.record.dto.MyRecordResponse;
import busim.kkilogbu.user.appple.domain.dto.*;
import busim.kkilogbu.user.dto.RequestUserCategory;
import busim.kkilogbu.user.dto.RequestUserNickname;
import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.dto.UserInfoResponse;
import busim.kkilogbu.user.entity.LoginType;
import busim.kkilogbu.user.service.UserPhoneService;
import busim.kkilogbu.user.service.UserService;
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

import java.util.Optional;

@RestController
@RequestMapping("/kkilogbu/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserPhoneService userPhoneService;


    @PostMapping("/signin/apple")
    public ResponseEntity<?> appleSignIn(@RequestBody AppleSignInRequest request) {
        try {
            String authorizationCode = request.getAuthorizationCode();
            String identityToken = request.getIdentityToken();
            String phoneIdentificationNumber = request.getPhoneIdentificationNumber();

            // 동일한 phoneIdentificationNumber가 있는지 확인
            Optional<LoginType> existingLoginType = userPhoneService.checkUserByPhoneIdentification(phoneIdentificationNumber);

            if (existingLoginType.isPresent()) {
                // 동일한 번호가 있으면 200 응답과 함께 소셜 로그인 타입 반환
                return ResponseEntity.ok("이미 등록된 번호입니다. 로그인 타입: " + existingLoginType.get());
            }

            // 동일한 번호가 없으면 애플 로그인 진행
            SignInResponse result = userService.appleSignIn(authorizationCode, identityToken, phoneIdentificationNumber);
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

    @PostMapping("/login/{loginType}")
    public ResponseEntity<?> appleAutomaticLogin(
            @PathVariable("loginType") LoginType loginType,  // Enum으로 직접 받음
            @RequestBody AutomaticLoginRequest request) {

        try {
            String deviceIdentificationCode = request.getDeviceIdentificationCode();
            String accessToken = request.getAccessToken();

            // 자동 로그인 처리
            LoginResponse result = userService.automaticLogin(deviceIdentificationCode, accessToken, loginType);
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다: " + e.getMessage());
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
