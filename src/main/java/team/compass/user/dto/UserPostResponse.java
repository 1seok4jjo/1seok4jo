package team.compass.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.compass.post.controller.response.PostResponse;

import java.util.List;

@Getter
@Setter
@Builder
public class UserPostResponse {
    private Long count;
    private List<PostResponse> postResponseList;
}
