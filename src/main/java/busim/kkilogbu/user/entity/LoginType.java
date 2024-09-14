package busim.kkilogbu.user.entity;



import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public enum LoginType {

    GOOGLE("구글 로그인"),
    KAKAO("카카오 로그인"),
    APPLE("애플 로그인"),
    ANONYMOUS("비회원 로그인");

    private final String description;

    LoginType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

