package team.compass.comment.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    private Integer commentId;
    private Integer userId;
    private String nickName;
    private String content;
    private String imageUrl;
    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;


}
