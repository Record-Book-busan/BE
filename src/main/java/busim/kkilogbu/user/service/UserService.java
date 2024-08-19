package busim.kkilogbu.user.service;

import busim.kkilogbu.user.dto.UserDto;
import busim.kkilogbu.user.dto.UserInfoResponse;
import busim.kkilogbu.user.entity.User;
import busim.kkilogbu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User userInfo(UserDto userDto) {
        userDto.toUser(userDto);
        return userRepository.save(userDto.toUser(userDto));
    }

    public void changeUsername(String username){
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().build();

        User user = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 아이디 입니다")
        );
        user.changeUsername(username);
    }

    public UserInfoResponse getUserInfo() {
        // TODO : 로그인 기능 구현시 세션에서 유저 정보 가져오기
        User tmp = User.builder().build();

        User user = userRepository.findByUsername(tmp.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 아이디 입니다")
        );
        return UserInfoResponse.builder()
                .nickname(user.getNickname())
                .imageUrl(user.getProfileImage())
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
}
