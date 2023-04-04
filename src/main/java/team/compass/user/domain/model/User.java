package team.compass.user.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.compass.user.domain.SignUpInput;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId", nullable = false)
    private Integer userId;

    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String nickName;
    private String introduction;
    private String emailAuthKey;
    private String resetPasswordKey;
    private String kakaoId;
    private String userBannerImgUrl;
    private String userProfileImgUrl;

    public static User from(SignUpInput parameter) {
        return User.builder()
                .email(parameter.getEmail().toLowerCase(Locale.ROOT))
                .password(parameter.getPassword())
                .nickName(parameter.getNickName())
                .build();
    }
}
