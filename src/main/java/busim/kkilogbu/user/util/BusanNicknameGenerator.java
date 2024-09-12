package busim.kkilogbu.user.util;

import busim.kkilogbu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;


import java.util.Random;


@RequiredArgsConstructor
public class BusanNicknameGenerator implements NicknameGeneratorStrategy{

    private final UserRepository userRepository;


    private static final String[] ADJECTIVES = {
            "푸른", "바다의", "항구의", "멋진", "활기찬", "바람의", "빛나는", "따뜻한", "햇살의", "달빛의",
            "산뜻한", "고요한", "반짝이는", "용감한", "청명한", "시원한", "자유로운", "낭만적인"
    };

    private static final String[] NOUNS = {
            "해운대", "광안리", "태종대", "영도", "자갈치", "국밥", "갈매기", "송정", "동백꽃", "다대포",
            "BIFF", "송도", "서면", "범어사", "동래", "부평시장", "흰여울", "누리마루", "해동용궁사", "초량",
            "부산타워", "감천문화마을", "송정해수욕장", "부산항", "센텀시티", "온천천", "기장", "영도다리"
    };

    @Override
    public String generateNickname() {
        Random random = new Random();
        String nickname = generateRandomNickname(random);

        // 중복된 닉네임이 있는 경우, 새로 닉네임을 생성
        while (userRepository.existsByNickname(nickname)) {
            nickname = generateRandomNickname(random);
        }

        return nickname;
    }

    // 랜덤 닉네임을 생성하는 메서드
    private String generateRandomNickname(Random random) {
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        return adjective + noun;
    }

}
