package team.compass.user.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdate {
    private String password;
    private String nickName;
    private String accessToken;
}
