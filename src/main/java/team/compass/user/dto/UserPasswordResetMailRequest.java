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
public class UserPasswordResetMailRequest {
    @NotEmpty(message = "이메일 입력은 필수입니다.")
    private String email;
}
