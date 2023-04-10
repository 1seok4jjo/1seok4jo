package team.compass.member.dto;

import lombok.*;
import team.compass.member.domain.Member;

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

    public static MemberResponseDto to(Member member, String accessToken) {
        return MemberResponseDto.builder()
                .userId(member.getUserId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .accessToken(accessToken)
                .build();
    }
}
