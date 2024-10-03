package busim.kkilogbu.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "유저 닉네임 수정 요청 데이터 객체")
public class UserNameUpdateRequest {
	@Schema(description = "유저 닉네임", example = "닉네임")
	private String nickName;
}
