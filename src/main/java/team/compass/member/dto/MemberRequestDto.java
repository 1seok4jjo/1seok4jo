package team.compass.member.dto;

import lombok.Builder;
import lombok.Data;
import team.compass.member.domain.User;

import javax.validation.constraints.NotEmpty;

public class MemberRequestDto {
    @Data
    @Builder
    public static class SignIn {
        private String email;
        private String password;
    }

    @Data
    @Builder
    public static class SignUp {
        @NotEmpty(message = "이메일 입력은 필수입니다.")
        private String email;
        @NotEmpty(message = "비밀번호 입력은 필수입니다.")
        private String password;
        @NotEmpty(message = "닉네임 입력은 필수입니다.")
        private String nickname;

        public static User toEntity(SignUp parameter) {
            return User.builder()
                    .email(parameter.getEmail())
                    .password(parameter.getPassword())
                    .nickName(parameter.getNickname())
                    .build();
        }
    }
}
