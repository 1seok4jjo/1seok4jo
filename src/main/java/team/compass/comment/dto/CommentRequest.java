package team.compass.comment.dto;



import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.compass.comment.domain.Comment;
import team.compass.post.domain.Post;
import team.compass.user.domain.User;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotNull
    private Integer userId;
    private Integer postId;
    @NotBlank
    private String content;

    private LocalDateTime createdTime;

    public Comment requestComment(Post posting, User user){
        return Comment.builder()
            .post(posting)
            .user(user)
            .content(this.content)
            .createdTime(LocalDateTime.now())
            .build();
    }


}
