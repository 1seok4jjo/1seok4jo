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
    private final String baseUrl = "https://compass-s3-bucket.s3.ap-northeast-2.amazonaws.com/";
    private String userBannerImgUrl;
    private String userProfileImgUrl;
}
