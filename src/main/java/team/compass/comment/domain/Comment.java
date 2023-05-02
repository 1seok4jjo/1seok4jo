package team.compass.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.compass.post.domain.Post;
import team.compass.user.domain.User;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @Column(nullable = false,name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne //comment(M) User(O)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne //comment(M) Post(O)
    @JoinColumn(name = "post_id")
    private Post post;
    private String nickName;
    private String content;
    private String imageUrl;
    public void updateContent(String content) {
        this.content = content;
    }
}

