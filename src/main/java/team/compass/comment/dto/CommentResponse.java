package team.compass.comment.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import team.compass.user.domain.User;
import team.compass.comment.domain.Comment;

@Data
@Builder
public class CommentResponse {
    private Integer commentId;
    private Integer userId;
    private String nickName;
    private String content;
    private String imageUrl;
    private LocalDateTime createdTime;



    public static CommentResponse responseComment(Comment comment, User user) {

        CommentResponseBuilder builder = CommentResponse.builder()
            .commentId(comment.getId())
            .userId(user.getId())
            .nickName(user.getNickName())
            .imageUrl(user.getProfileImageUrl())
            .content(comment.getContent())
            .createdTime(comment.getCreatedTime());
        return  builder.build();
    }
    public static CommentResponse fromEntity(Comment comment) {

        CommentResponseBuilder builder = CommentResponse.builder()
            .commentId(comment.getId())
            .userId(comment.getUser().getId())
            .nickName(comment.getUser().getNickName())
            .imageUrl(comment.getUser().getProfileImageUrl())
            .content(comment.getContent())
            .createdTime(comment.getCreatedTime());
        return  builder.build();
    }


}
