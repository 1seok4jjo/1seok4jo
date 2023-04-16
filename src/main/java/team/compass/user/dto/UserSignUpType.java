package team.compass.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum UserSignUpType {
    NORMAL("USER_NORMAL"),
    KAKAO("USER_KAKAO");

    private String signUpType;
}
