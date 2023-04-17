package team.compass.user.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserUpdate {
    private String password;
    private String introduction;
    private String userBannerImgUrl;
    private String userProfileImgUrl;
}
