package busim.kkilogbu.user.dto;

import busim.kkilogbu.sociaLogin.appple.domain.dto.SignInResponse;
import busim.kkilogbu.user.entity.users.Users;
import lombok.Getter;

import java.time.Instant;

@Getter
public class SignInResponseMapper {


    // 발급 시간을 포함한 SignInResponse 생성 메서드
    public static SignInResponse toSignInResponse(Users users, String accessToken, String refreshToken, Instant issuedAt, Boolean isAgreed) {
        return SignInResponse.builder()
                .userId(users.getId())
                .accessToken("Bearer " + accessToken)
                .refreshToken(refreshToken)
                .accessTokenAt(issuedAt)  // 발급 시간 추가
                .isAgreed(isAgreed)
                .build();
    }

    // 발급 시간을 함께 처리하는 createSignInResponse 메서드
    public static SignInResponse createSignInResponse(Users users, String accessToken, String refreshToken,Boolean isAgreed) {
        Instant issuedAt = Instant.now();  // 발급 시간 저장 (현재 시간)

        // 발급 시간과 함께 SignInResponse 반환
        return SignInResponseMapper.toSignInResponse(
                        users,
                        accessToken,
                        refreshToken,
                issuedAt,
                isAgreed  // 발급 시간 전달

                );
    }
}
