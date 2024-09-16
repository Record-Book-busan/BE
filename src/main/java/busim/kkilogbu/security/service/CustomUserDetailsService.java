package busim.kkilogbu.security.service;

import busim.kkilogbu.security.domain.CustomUserDetails;
import busim.kkilogbu.user.entity.users.User;
import busim.kkilogbu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 이메일을 통해 데이터베이스에서 사용자 정보 조회
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("이메일로 사용자를 찾을 수 없습니다: " + email);
        }
        // 비밀번호가 없는 소셜 로그인 사용자 처리
        return new CustomUserDetails(user);
    }
}