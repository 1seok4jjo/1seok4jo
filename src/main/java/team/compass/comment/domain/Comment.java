package team.compass.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.compass.photo.util.post.domain.Post;
import team.compass.user.domain.User;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;
    @ManyToOne //comment(M) User(O)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne //comment(M) Post(O)
    @JoinColumn(name = "post_id")
    private Post post;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updateTime;
    public void updateContent(String content) {
        this.content = content;
    }
}

