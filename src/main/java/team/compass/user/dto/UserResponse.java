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
    private String introduction;
    private String profileUrl;
    private String bannerUrl;
    private String accessToken;

    public static UserResponse to(User user, String accessToken) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .introduction(user.getIntroduction())
                .profileUrl(user.getProfileImageUrl())
                .bannerUrl(user.getUserBannerImgUrl())
                .accessToken(accessToken)
                .build();
    }
}
