package team.compass.search.dto;

import lombok.Getter;
import lombok.Setter;
import team.compass.post.domain.Post;

import java.util.List;

@Getter
@Setter
public class SearchResponse {
    private String keyword;

    private Long count;
    private List<Post> searchPostList;
}
