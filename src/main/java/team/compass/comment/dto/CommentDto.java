package team.compass.comment.dto;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long commentId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

}
