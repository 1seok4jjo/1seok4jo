package team.compass.post.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.compass.post.entity.Post;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {

    private String title; // 제목
    private String detail; // 내용
    private String location;

    private Long postThemes; // 테마 - 테마 id로 매핑할 예정 (toEntity 에 삽입할 예정입니다.)
    private String startDate; // 여행 시작
    private String endDate; // 여행 끝
    private String hashtag; // 해시태그 (프론트에서 받아옴, #마다 잘라서 문자로 들어옴)


    // DTO - ENTITY
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .detail(detail)
                .location(location) // 지역
                .hashtag(hashtag) // 해시태그
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
