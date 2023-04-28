package team.compass.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import team.compass.post.controller.response.PostResponse;
import team.compass.post.domain.Post;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class UserPostResponse {
    private Long count;
    private List<PostResponse> postResponseList;

    public static UserPostResponse build(Page<Post> postPage) {
        return UserPostResponse.builder()
                .count(postPage.getTotalElements())
                .postResponseList(
                        postPage.stream().map(item ->
                            PostResponse.builder()
                                .id(item.getId())
                                .title(item.getTitle())
                                .detail(item.getDetail())
                                .hashtag(item.getHashtag())
                                .location(item.getLocation())
                                .createdAt(item.getCreatedAt())
                                    .commentCount((long) item.getContents().size())
                                    .likeCount(item.getLikes().size())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }
}
