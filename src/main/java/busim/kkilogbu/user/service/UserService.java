package busim.kkilogbu.user.service;

import busim.kkilogbu.bookmark.dto.BookmarkResponse;
import busim.kkilogbu.bookmark.repository.BookmarkRepository;
import busim.kkilogbu.record.dto.MyRecordResponse;
import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.dto.UserInfoRequest;
import busim.kkilogbu.user.dto.UserInfoResponse;
import busim.kkilogbu.user.entity.User;
import busim.kkilogbu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final BookmarkRepository bookmarkRepository;

    public User userInfo(UserDto userDto) {
        userDto.toUser(userDto);
        return userRepository.save(userDto.toUser(userDto));
    }

    public void changeUserInfo(UserInfoRequest request){
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().build();

        User user = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 아이디 입니다")
        );
        if(request.getNickname() != null) {
            user.changeNickname(request.getNickname());
        }
        if(request.getProfileImage() != null){
            user.changeProfileImage(request.getProfileImage());
        }
    }

    public UserInfoResponse getUserInfo() {
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().build();

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

    public void changeCategory(Long category) {
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().build();

        User user = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 아이디 입니다")
        );
        user.categoryChange(category);
    }

    public List<MyRecordResponse> getMyRecord() {
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().build();

        User user = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 아이디 입니다")
        );
        return recordRepository.findByUser(user).stream().map((record) -> MyRecordResponse.builder()
                .id(record.getId())
                .title(record.getContents().getTitle())
                .content(record.getContents().getContent())
                .imageUrl(record.getContents().getImageUrl())
                .cat2(record.getCat2())
                .lat(record.getAddressInfo().getLatitude())
                .lng(record.getAddressInfo().getLongitude())
                .build()).collect(Collectors.toList());

    }

    public List<BookmarkResponse> getBookmark() {
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().build();

         User user = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 아이디 입니다"));
        return bookmarkRepository.findByUser(user).stream().map((bookmark -> {
            if(bookmark.isRecord()){
                return BookmarkResponse.builder()
                        .id(bookmark.getId())
                        .title(bookmark.getRecord().getContents().getTitle())
                        .address(bookmark.getRecord().getAddressInfo().getAddress())
                        .cat1(bookmark.getRecord().getCat1())
                        .cat2(bookmark.getRecord().getCat2())
                        .build();
            }else{
                return BookmarkResponse.builder()
                        .id(bookmark.getId())
                        .title(bookmark.getPlace().getContents().getTitle())
                        .address(bookmark.getPlace().getAddressInfo().getAddress())
                        .cat1(bookmark.getPlace().getCat1())
                        .cat2(bookmark.getPlace().getCat2())
                        .build();
            }
        })).collect(Collectors.toList());
    }
}
