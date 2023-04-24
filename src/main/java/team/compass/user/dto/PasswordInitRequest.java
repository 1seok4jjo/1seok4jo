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
public class PasswordInitRequest {
    @NotEmpty(message = "기존 패스워드 입력은 필수입니다.")
    String password;
    @NotEmpty(message = "새로운 패스워드 입력은 필수입니다.")
    String newPassword;
}
