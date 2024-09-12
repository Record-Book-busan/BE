package busim.kkilogbu.user.service;

import busim.kkilogbu.user.entity.LoginType;
import busim.kkilogbu.user.entity.users.User;
import busim.kkilogbu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPhoneService {

    private final UserRepository userRepository;

    // 동일한 phoneIdentificationNumber로 유저가 있는지 확인하는 메서드
    public Optional<LoginType> checkUserByPhoneIdentification(String phoneIdentificationNumber) {
        // 데이터베이스에서 phoneIdentificationNumber로 유저를 조회
        Optional<User> existingUser = userRepository.findByPhoneIdentificationNumber(phoneIdentificationNumber);

        // 유저가 존재하면 해당 유저의 로그인 타입 반환
        return existingUser.map(User::getLoginType);
    }

}
