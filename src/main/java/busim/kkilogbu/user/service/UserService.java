package busim.kkilogbu.user.service;

import busim.kkilogbu.user.dto.UserDto;
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
}
