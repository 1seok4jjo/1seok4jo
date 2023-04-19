package team.compass.search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.compass.post.controller.response.PostResponse;
import team.compass.post.domain.Post;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchResponse {
    private String keyword;
    private Long count;
    private List<PostResponse> searchPostList;


}
