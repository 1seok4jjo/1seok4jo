package team.compass.comment.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    private Long commentId;
    private Long UserId;
    private String NickName;
    private String content;
    private String imageUrl;
    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;


}
