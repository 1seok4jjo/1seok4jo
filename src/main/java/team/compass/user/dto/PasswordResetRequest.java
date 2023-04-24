package team.compass.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
    @NotEmpty(message = "패스워드 입력은 필수입니다.")
    String password;
    @NotEmpty(message = "초기화 코드 입력은 필수입니다.")
    String uuid;
}
