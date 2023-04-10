package team.compass.member.dto;

import lombok.*;
import team.compass.member.domain.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Integer userId;
    private String email;
    private String nickName;
    private String accessToken;

    public static MemberResponseDto to(User user, String accessToken) {
        return MemberResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .accessToken(accessToken)
                .build();
    }
}
