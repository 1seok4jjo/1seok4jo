package team.compass.user.dto;

import lombok.*;
import team.compass.user.domain.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer userId;
    private String email;
    private String nickName;

    public static UserResponse to(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .build();
    }
}
