package team.compass.comment.dto;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotNull
    private Long userId;
    private Long postId;
    @NotBlank
    private String commentOfDetail;

}
