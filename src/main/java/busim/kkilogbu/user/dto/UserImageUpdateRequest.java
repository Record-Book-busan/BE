package busim.kkilogbu.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "유저 이미지 수정 요청 데이터 객체")
public class UserImageUpdateRequest {
	@Schema(description = "유저 프로필 이미지 URL", example = "userImage/a3821089-02ef-4a3c-946d-6e3bf3456658.png")
	private String profileImage;
}
