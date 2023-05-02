package team.compass.user.dto;

import lombok.*;
import team.compass.user.domain.User;

import javax.validation.constraints.NotEmpty;

public class UserRequest {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignIn {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {
        @NotEmpty(message = "이메일 입력은 필수입니다.")
        private String email;
        @NotEmpty(message = "비밀번호 입력은 필수입니다.")
        private String password;
        @NotEmpty(message = "닉네임 입력은 필수입니다.")
        private String nickname;

        private String profileImageUrl;
        private String userBannerImgUrl;

        private String loginType;

        public static User toEntity(SignUp parameter) {
            return User.builder()
                    .email(parameter.getEmail())
                    .password(parameter.getPassword())
                    .nickName(parameter.getNickname())
                    .loginType(parameter.getLoginType())
                    .profileImageUrl(parameter.getProfileImageUrl())
                    .userBannerImgUrl(parameter.getUserBannerImgUrl())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Logout {
        private String email;
        private String accessToken;
    }
}
