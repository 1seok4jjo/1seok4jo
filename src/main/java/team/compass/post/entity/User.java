package team.compass.post.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String introduction;

    private String emailAuthKey;

    private String resetPasswordKey;

    private String userBannerImgUrl;

    private String userProfileImgUrl;

    private String loginType; // service or kakao

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Content> contents;

    @OneToMany(mappedBy = "chattingRoom")
    private List<ChattingRoomUser> chattingRoomUsers;

    @OneToMany(mappedBy = "user")
    private List<Likes> likes;

    @OneToMany(mappedBy = "user")
    private List<Photo> photos;

    public User(Long id, String email, String password, String nickname, String introduction, String emailAuthKey,
                String resetPasswordKey, String userBannerImgUrl, String userProfileImgUrl, String loginType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.introduction = introduction;
        this.emailAuthKey = emailAuthKey;
        this.resetPasswordKey = resetPasswordKey;
        this.userBannerImgUrl = userBannerImgUrl;
        this.userProfileImgUrl = userProfileImgUrl;
        this.loginType = loginType;
    }
}