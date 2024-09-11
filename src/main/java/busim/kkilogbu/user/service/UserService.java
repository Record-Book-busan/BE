package busim.kkilogbu.user.service;

import busim.kkilogbu.bookmark.dto.BookmarkResponse;
import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.bookmark.repository.BookmarkRepository;

import busim.kkilogbu.record.dto.MyRecordResponse;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.user.appple.controller.AppleClient;
import busim.kkilogbu.user.appple.domain.dto.AppleRevokeRequest;
import busim.kkilogbu.user.appple.domain.dto.AppleTokenRequest;
import busim.kkilogbu.user.appple.domain.dto.AppleTokenResponse;
import busim.kkilogbu.user.appple.domain.dto.SignInResponse;
import busim.kkilogbu.user.appple.service.AppleAuthService;
import busim.kkilogbu.user.appple.service.AppleTokenService;
import busim.kkilogbu.user.dto.SignInResponseMapper;
import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.dto.UserInfoRequest;
import busim.kkilogbu.user.dto.UserInfoResponse;
import busim.kkilogbu.user.entity.LoginType;
import busim.kkilogbu.user.entity.User;
import busim.kkilogbu.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final BookmarkRepository bookmarkRepository;

    private final AppleClient appleClient;
    private final AppleTokenService appleTokenService;
    private final AppleAuthService appleAuthService;

    public User userInfo(UserDto userDto) {
        userDto.toUser(userDto);
        return userRepository.save(userDto.toUser(userDto));
    }

    // 애플 로그인 처리
    @Transactional(readOnly = false)
    public SignInResponse appleSignIn(String authorizationCode, String identityToken) throws Exception {
        // 1. identityToken을 사용해 JWT 검증 (애플의 공개 키 사용)
        Claims claims = appleTokenService.verifyIdentityToken(identityToken);
        String appleUserId = claims.getSubject();  // JWT에서 애플 사용자 ID 추출

        log.info("JWT 검증 성공!! 애플 사용자 ID: " + appleUserId);


        /// 2. AppleTokenResponse 객체를 사용해 애플 서버에서 access token 교환
        AppleTokenResponse tokenResponse = appleAuthService.getAppleToken(authorizationCode);
        log.info("access token 교환 성공!! : " + tokenResponse);

       // 3. DB에서 사용자 조회 (존재하지 않으면 새로 생성)
        User user = userRepository.findByAppleUserId(appleUserId)
                .orElseGet(() -> {
                    log.info("애플 사용자 ID " + appleUserId + "를 가진 사용자가 존재하지 않음, 새 사용자 생성");
                    User newUser = registerNewUser(claims);
                    log.info("새 사용자 생성, ID 값은 자동 할당됩니다. 이메일: " + newUser.getEmail());
                    newUser.updateTokens(tokenResponse.refreshToken(), tokenResponse.accessToken()); // 토큰도 함께 업데이트
                    User savedUser = userRepository.save(newUser);  // 새로 생성된 사용자 저장
                    log.info("새 사용자 저장 완료. 사용자 ID: " + savedUser.getId());
                    log.info("새로운 애플 사용자 저장 완료: " + savedUser.getId());
                    return savedUser;
                });

        // 4. 기존 사용자일 경우 토큰 업데이트 후 저장
        if (user.getAppleUserId()!= null) {  // 기존 사용자인 경우
            user.updateTokens(tokenResponse.refreshToken(), tokenResponse.accessToken());
            userRepository.save(user);  // 업데이트된 사용자 정보 저장
        }

        // 5. 매퍼를 통해 SignInResponse 반환
        return SignInResponseMapper.toSignInResponse(user, tokenResponse);
    }

    private User registerNewUser(Claims claims) {
        // 이메일 정보가 없을 수 있으므로 예외 처리
        String email = claims.get("email", String.class);

        if (email == null) {
            log.warn("애플 로그인: 이메일 정보가 없습니다.");
            // 이메일이 없는 경우 처리 방법: 예를 들어 기본 이메일 설정
            email = "unknown@apple.com"; // 임시 이메일 설정 (필요시 사용자에게 입력 요청)
        }

        return User.builder()
                .appleUserId(claims.getSubject())  // 애플 사용자 ID 설정
                .email(email)  // 이메일 설정
                .loginType(LoginType.APPLE)
                .build();
    }





    public UserInfoResponse getUserInfo() {
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().username("tmp").build();

        User user = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 아이디 입니다")
        );
        return UserInfoResponse.builder()
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
        .build();
    }

    public Boolean checkUsernameDuplicate(String name) {
        return userRepository.existsByNickname(name);
    }

    @Transactional
    public void changeCategory(Long category) {
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().username("tmp").build();

        User user = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 아이디 입니다")
        );
        user.categoryChange(category);
    }

    public Slice<MyRecordResponse> getMyRecord(Pageable pageable) {
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().username("tmp").build();

        User user = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 아이디 입니다")
        );

        Slice<Records> findUserRecords = recordRepository.findByUser(user, pageable);
        return findUserRecords.map((record) -> {
            return MyRecordResponse.builder()
                .id(record.getId())
                .title(record.getContents().getTitle())
                .content(record.getContents().getContent())
                .imageUrl(record.getContents().getImageUrl())
                .lat(record.getAddressInfo().getLatitude())
                .lng(record.getAddressInfo().getLongitude())
                .build();
        });
    }

    public Slice<BookmarkResponse> getBookmark(Pageable pageable, String type) {
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().username("tmp").build();

         User user = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 아이디 입니다"));

         Slice<Bookmark> findUserBookmark = null;
         if(type.equals("RECORD")){
            findUserBookmark = bookmarkRepository.findByUserAndRecordIsNotNull(user, pageable);
            return findUserBookmark.map((bookmark -> {
                return getBookmarkResponse(bookmark.getId(), bookmark.getRecords().getContents().getTitle(),
                    bookmark.getRecords().getAddressInfo().getAddress());
            }));
         }else if(type.equals("PLACE")){
            findUserBookmark = bookmarkRepository.findByUserAndPlaceIsNotNull(user, pageable);
            return findUserBookmark.map((bookmark -> {
                return getBookmarkResponse(bookmark.getId(), bookmark.getPlace().getContents().getTitle(),
                    bookmark.getPlace().getAddressInfo().getAddress());
            }));
         }else{
            throw new RuntimeException("잘못된 타입입니다");
         }
    }

    private BookmarkResponse getBookmarkResponse(Long id, String title, String address) {
        return BookmarkResponse.builder()
            .id(id)
            .title(title)
            .address(address)
            .build();
    }

    @Transactional
    public void deleteUserAccount(Long userId, String accessToken) {
        // 1. 애플 OAuth 토큰 해지 (revoke)
        revokeAppleToken(accessToken);

        // 2. 데이터베이스에서 사용자 정보 삭제
        userRepository.deleteById(userId);
    }

    private void revokeAppleToken(String accessToken) {
        try {
            // AppleRevokeRequest 객체 생성
            AppleRevokeRequest appleRevokeRequest = AppleRevokeRequest.builder()
                    .clientId("com.busim.recordbookbusan")  // 클라이언트 ID (Bundle ID)
                    .clientSecret(this.createSecret())  // JWT로 서명된 client_secret 생성
                    .token(accessToken)  // 해지할 OAuth 액세스 토큰
                    .tokenTypeHint("access_token")  // 해지할 토큰 유형 (access_token)
                    .build();

            // FeignClient를 통해 애플 토큰 해지 API 호출
            appleClient.revoke(appleRevokeRequest);

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Apple 회원탈퇴 실패 했습니다: " + e.getMessage());
        }
    }

    // client_secret 생성 메소드 (JWT 서명)
    private String createSecret() {
        // 30일 동안 유효한 JWT 생성 (createSecret 로직 참고)
        // 생략: 앞에서 설명한 createSecret 로직 재사용
        return "생성된 client_secret";  // JWT 생성 로직에 맞게 구현 필요
    }
}
