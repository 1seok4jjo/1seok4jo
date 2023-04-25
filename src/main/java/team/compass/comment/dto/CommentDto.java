package team.compass.comment.dto;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Integer commentId;
    private Integer userId;
    private String nickName;
    private String content;
    private String imageUrl;
    private LocalDateTime createdTime;



}
