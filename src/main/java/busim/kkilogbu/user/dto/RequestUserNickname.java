package busim.kkilogbu.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RequestUserNickname {
	@NotNull(message = "닉네임을 입력해주세요.")
	private String nickName;
}
