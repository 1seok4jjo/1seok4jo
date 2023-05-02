package team.compass.comment.dto;



import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.compass.comment.domain.Comment;
import team.compass.post.domain.Post;
import team.compass.user.domain.User;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotNull
    private Integer userId;
    private Integer postId;
    @NotBlank
    private String content;

    public Comment requestComment(Post posting, User user){
        return Comment.builder()
            .post(posting)
            .user(user)
            .content(this.content)
            .build();
    }
}
