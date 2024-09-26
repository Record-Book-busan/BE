package busim.kkilogbu.user.service;

import busim.kkilogbu.bookmark.repository.BookmarkRepository;

import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.security.domain.CustomUserDetails;
import busim.kkilogbu.security.service.CustomUserDetailsService;
import busim.kkilogbu.sociaLogin.appple.controller.AppleClient;
import busim.kkilogbu.sociaLogin.appple.domain.dto.*;
import busim.kkilogbu.sociaLogin.appple.service.AppleAuthService;
import busim.kkilogbu.sociaLogin.appple.service.AppleLoginService;
import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.entity.users.Users;
import busim.kkilogbu.user.entity.UserConsent;
import busim.kkilogbu.user.repository.UserConsentRepository;
import busim.kkilogbu.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;




@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final RecordRepository recordRepository;
    private final BookmarkRepository bookmarkRepository;


    private final AppleClient appleClient;

    private final AppleAuthService appleAuthService;
    private final UserConsentRepository userConsentRepository;
    private final AppleLoginService appleLoginService;

    public Users userInfo(UserDto userDto) {
        userDto.toUser(userDto);
        return userRepository.save(userDto.toUser(userDto));
    }






    @Transactional
    public void saveUserConsent(String socialUserId, UserConsentRequest consentRequest) {
        Users users = userRepository.findBySocialUserId(socialUserId)
                .orElseThrow(() -> new EntityNotFoundException("유저 정보가 없습니다"));

        UserConsent consent = UserConsent.builder()
                .users(users)  // Users 객체 설정
                .termsAgreed(consentRequest.isTermsAgreed())
                .privacyAgreed(consentRequest.isPrivacyAgreed())
                .build();

        userConsentRepository.save(consent);
    }


//    public UserInfoResponse getUserInfo() {
//        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
//        Users tmp = Users.builder().username("tmp").build();
//
//        Users users = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
//            () -> new RuntimeException("존재하지 않는 아이디 입니다")
//        );
//        return UserInfoResponse.builder()
//                .nickname(users.getNickname())
//                .profileImage(users.getProfileImage())
//        .build();
//    }

//    public Boolean checkUsernameDuplicate(String name) {
//        return userRepository.existsByNickname(name);
//    }
//
//    @Transactional
//    public void changeCategory(Long category) {
//        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
//        Users tmp = Users.builder().username("tmp").build();
//
//        Users users = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
//            () -> new RuntimeException("존재하지 않는 아이디 입니다")
//        );
//        users.categoryChange(category);
//    }
//
//    public Slice<MyRecordResponse> getMyRecord(Pageable pageable) {
//        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
//        Users tmp = Users.builder().username("tmp").build();
//
//        Users users = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
//            () -> new RuntimeException("존재하지 않는 아이디 입니다")
//        );
//
//        Slice<Records> findUserRecords = recordRepository.findByUser(users, pageable);
//        return findUserRecords.map((record) -> {
//            return MyRecordResponse.builder()
//                .id(record.getId())
//                .title(record.getContents().getTitle())
//                .content(record.getContents().getContent())
//                .imageUrl(record.getContents().getImageUrl())
//                .lat(record.getAddressInfo().getLatitude())
//                .lng(record.getAddressInfo().getLongitude())
//                .build();
//        });
//    }
//
//    public Slice<BookmarkResponse> getBookmark(Pageable pageable, String type) {
//        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
//        Users tmp = Users.builder().username("tmp").build();
//
//         Users users = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
//            () -> new RuntimeException("존재하지 않는 아이디 입니다"));
//
//         Slice<Bookmark> findUserBookmark = null;
//         if(type.equals("RECORD")){
//            findUserBookmark = bookmarkRepository.findByUserAndRecordIsNotNull(users, pageable);
//            return findUserBookmark.map((bookmark -> {
//                return getBookmarkResponse(bookmark.getId(), bookmark.getRecords().getContents().getTitle(),
//                    bookmark.getRecords().getAddressInfo().getAddress());
//            }));
//         }else if(type.equals("PLACE")){
//            findUserBookmark = bookmarkRepository.findByUserAndPlaceIsNotNull(users, pageable);
//            return findUserBookmark.map((bookmark -> {
//                return getBookmarkResponse(bookmark.getId(), bookmark.getPlace().getContents().getTitle(),
//                    bookmark.getPlace().getAddressInfo().getAddress());
//            }));
//         }else{
//            throw new RuntimeException("잘못된 타입입니다");
//         }
//    }
//
//    private BookmarkResponse getBookmarkResponse(Long id, String title, String address) {
//        return BookmarkResponse.builder()
//            .id(id)
//            .title(title)
//            .address(address)
//            .build();
//    }

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

    public Users getCurrentUser(){
        String name = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getName();
		CustomUserDetails customUserDetails = (CustomUserDetails)customUserDetailsService.loadUserByUsername(name);
        return customUserDetails.getUsers();
    }
}
