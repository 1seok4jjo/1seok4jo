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
}
