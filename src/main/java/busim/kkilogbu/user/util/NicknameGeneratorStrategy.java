package busim.kkilogbu.user.util;

import org.springframework.stereotype.Service;

@Service
public interface NicknameGeneratorStrategy {

    String generateNickname();
}
