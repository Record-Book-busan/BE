package busim.kkilogbu.user.dto;

import busim.kkilogbu.user.entity.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private Long id;
    private String username;
    private String nickname;


    public User toUser(UserDto userDto){
        return User.builder()
                .id(id)
                .username(userDto.username)
                .nickname(userDto.nickname)
                .build();
    }


}
