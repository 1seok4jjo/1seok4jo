package team.compass.comment.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import team.compass.comment.domain.Comment;
import team.compass.user.domain.User;

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

    public static CommentResponse responseComment(Comment comment, User writer) {

        CommentResponseBuilder builder = CommentResponse.builder()
            .commentId(comment.getCommentId())
            .userId(writer.getUserId())
            .nickName(writer.getNickName())
            .imageUrl(writer.getProfileImageUrl())
            .content(comment.getContent())
            .createdAt(comment.getCreatedTime())
            .updatedAt(comment.getUpdateTime());
        return builder.build();
    }
    public static CommentResponse fromEntity(Comment comment) {

        CommentResponseBuilder builder = CommentResponse.builder()
            .commentId(comment.getCommentId())
            .userId(comment.getWriter().getUserId())
            .nickName(comment.getWriter().getNickName())
            .imageUrl(comment.getWriter().getProfileImageUrl())
            .content(comment.getContent())
            .createdAt(comment.getCreatedTime())
            .updatedAt(comment.getUpdateTime());
        return builder.build();
    }


}
