package team.compass.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;


@Data
public class PostDto {
    private Integer postId;
    private Integer likeCount;
    private List<String> storeFileUrl;

    private String title;

    private String location;

    private String startDate;
    private String endDate;

    public PostDto(Integer postId, Integer likeCount, List<String> storeFileUrl, String title, String location, String startDate, String endDate) {
        this.postId = postId;
        this.likeCount = likeCount;
        this.storeFileUrl = storeFileUrl;
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}


// @Data
// static class PostDto {
//     @Override
//     public String toString() {
//         return "PostDto{" +
//             "postName=" + postName +
//             ", likeCount=" + likeCount +
//             ", photoName=" + photoName +
//             '}';
//     }
//
//     private Long postName;
//     private Integer likeCount;
//     private List<String> photoName;
//
//     public PostDto(Long postName, Integer likeCount, List<String> photoName) {
//         this.postName = postName;
//         this.likeCount = likeCount;
//         this.photoName = photoName;
//     }
// }