package team.compass.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;


@Data
public class PostDto {
    private Long postId;
    private Integer likeCount;
    private List<String> photoName;

    private String title;

    private String location;

    private String startDate;
    private String endDate;

    public PostDto(Long postId, Integer likeCount, List<String> photoName, String title, String location, String startDate, String endDate) {
        this.postId = postId;
        this.likeCount = likeCount;
        this.photoName = photoName;
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
