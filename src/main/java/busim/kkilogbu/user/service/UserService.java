package busim.kkilogbu.user.service;

import busim.kkilogbu.bookmark.dto.BookmarkResponse;
import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.bookmark.repository.BookmarkRepository;

import busim.kkilogbu.record.dto.MyRecordResponse;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.sociaLogin.appple.controller.AppleClient;
import busim.kkilogbu.sociaLogin.appple.domain.dto.*;
import busim.kkilogbu.sociaLogin.appple.service.AppleAuthService;
import busim.kkilogbu.sociaLogin.appple.service.AppleAutomaticLoginService;
import busim.kkilogbu.sociaLogin.appple.service.AppleTokenService;
import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.dto.UserInfoResponse;
import busim.kkilogbu.user.entity.LoginType;
import busim.kkilogbu.user.entity.users.User;
import busim.kkilogbu.user.entity.UserConsent;
import busim.kkilogbu.user.repository.UserConsentRepository;
import busim.kkilogbu.user.repository.UserRepository;
import busim.kkilogbu.user.util.NicknameGeneratorStrategy;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final BookmarkRepository bookmarkRepository;
    private final NicknameGeneratorStrategy nicknameGeneratorStrategy;

    private final AppleClient appleClient;
    private final AppleTokenService appleTokenService;
    private final AppleAuthService appleAuthService;
    private final AppleAutomaticLoginService appleAutomaticLoginService;
    private final UserConsentRepository userConsentRepository;

    public User userInfo(UserDto userDto) {
        userDto.toUser(userDto);
        return userRepository.save(userDto.toUser(userDto));
    }

//
//    @Transactional
//    public SignInResponse socialSignIn(String authorizationCode, String identityToken, LoginType loginType) throws Exception {
//        SocialLoginContext context = new SocialLoginContext();
//
//        switch (loginType) {
//            case APPLE:
//                context.setSocialLoginStrategy(new AppleLoginStrategy(appleTokenService, appleAuthService, userRepository));
//                break;
//            case GOOGLE:
//                context.setSocialLoginStrategy(new GoogleLoginStrategy(googleAuthService, userRepository));
//                break;
//            case KAKAO:
//                context.setSocialLoginStrategy(new KakaoLoginStrategy(kakaoAuthService, userRepository));
//                break;
//            case ANONYMOUS:
//                return handleAnonymousLogin();  // 익명 사용자 처리
//            default:
//                throw new IllegalArgumentException("지원하지 않는 로그인 타입입니다.");
//        }
//
//        return context.executeLogin(authorizationCode, identityToken);
//    }



    @Transactional
    public LoginResponse automaticLogin(String deviceIdentificationCode, String accessToken, LoginType loginType) throws Exception {
        Optional<User> userOptional = userRepository.findByPhoneIdentificationNumber(deviceIdentificationCode);

        if (userOptional.isEmpty()) {
            log.warn("[{} 로그인] 해당 기계식별코드로 등록된 유저가 없습니다. Device ID: {}", loginType, deviceIdentificationCode);
            throw new EntityNotFoundException("해당 기계식별코드로 등록된 유저가 없습니다.");
        }

        // TODO : 로그인 확인용
        User user = userOptional.get();

        // 로그인 타입에 따라 케이스를 나눔
        switch (loginType) {
            case APPLE:
                log.info("[애플 로그인] 애플 로그인 처리 시작. 유저ID: {}", user.getId());
                return appleAutomaticLoginService.handleAutomaticLogin(accessToken, user);

//            case GOOGLE:
//                log.info("[구글 로그인] 구글 로그인 처리 시작. 유저ID: {}", user.getId());
//                return handleGoogleLogin(user, accessToken);
//
//            case KAKAO:
//                log.info("[카카오 로그인] 카카오 로그인 처리 시작. 유저ID: {}", user.getId());
//                return handleKakaoLogin(user, accessToken);

            default:
                log.error("지원되지 않는 로그인 타입입니다: {}", loginType);
                throw new IllegalArgumentException("지원되지 않는 로그인 타입입니다.");
        }
    }


    @Transactional
    public void saveUserConsent(Long userId, UserConsentRequest consentRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저 정보가 없습니다"));

        UserConsent consent = UserConsent.builder()
                .user(user)  // User 객체 설정
                .termsAgreed(consentRequest.isTermsAgreed())
                .privacyAgreed(consentRequest.isPrivacyAgreed())
                .build();

        userConsentRepository.save(consent);
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
                    .clientSecret(appleAuthService.createClientSecret())  // JWT로 서명된 client_secret 생성
                    .token(accessToken)  // 해지할 OAuth 액세스 토큰
                    .tokenTypeHint("access_token")  // 해지할 토큰 유형 (access_token)
                    .build();

            // FeignClient를 통해 애플 토큰 해지 API 호출
            appleClient.revoke(appleRevokeRequest);

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Apple 회원탈퇴 실패 했습니다: " + e.getMessage());
        }
    }


}
