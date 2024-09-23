package busim.kkilogbu.user.dto;

import busim.kkilogbu.user.entity.users.Users;
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


    public Users toUser(UserDto userDto){
        return Users.builder()
                .id(id)
          //      .username(userDto.username)
                .build();
    }


}
