package team.compass.user.dto;

import lombok.*;
import team.compass.user.domain.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Integer userId;
    private String email;
    private String nickName;
    private String accessToken;

    public static UserResponseDto to(User user, String accessToken) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .accessToken(accessToken)
                .build();
    }
}
