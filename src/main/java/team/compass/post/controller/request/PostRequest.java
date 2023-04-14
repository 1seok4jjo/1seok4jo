package team.compass.post.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.compass.post.domain.Post;
import team.compass.theme.domain.Theme;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {


    private String title; // 제목
    private String detail; // 내용
    private String location;
    private String startDate; // 여행 시작
    private String endDate; // 여행 끝
    private String hashtag; // 해시태그 (프론트에서 받아옴, #마다 잘라서 문자로 들어옴)
    private Integer themeId;

    // DTO - ENTITY (toEntity를 DTO 에서 해주는 것.) Entity 는 테이블 그 자체이기에 건들면 위험함
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .detail(detail)
                .location(location) // 지역
                .hashtag(hashtag) // 해시태그
                .startDate(startDate)
                .endDate(endDate)
                .theme(new Theme(this.themeId))
                .createdAt(LocalDateTime.now())
                .build();
    }
}
