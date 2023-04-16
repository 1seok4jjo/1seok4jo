package team.compass.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.compass.user.domain.User;
import team.compass.post.domain.Post;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer commentId;
    @ManyToOne //comment(M) User(O)
    @JoinColumn
    private User writer;
    @ManyToOne //comment(M) Post(O)
    @JoinColumn
    private Post post;
    @Column
    private String content;
    @Column
    private LocalDateTime createdTime;
    private LocalDateTime updateTime;


    public void updateContent(String content) {
        this.content = content;
    }
}

