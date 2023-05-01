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

    public static UserPostResponse build(List<Post> postList) {
        return UserPostResponse.builder()
                .count((long) postList.size())
                .postResponseList(
                        postList.stream().map(item ->
                            PostResponse.builder()
                                .id(item.getId())
                                .title(item.getTitle())
                                .detail(item.getDetail())
                                .location(item.getLocation())
                                .createdAt(item.getCreatedAt())
                                .hashtag(item.getHashtag())
                                .startDate(item.getStartDate())
                                .endDate(item.getEndDate())
                                .commentCount((long) item.getContents().size())
                                .storeFileUrl(item.getPhotos().stream().map(i -> i.getPhoto().getStoreFileUrl()).collect(Collectors.toList()))
                                .likeCount(item.getLikes().size())
                                .themeId(item.getTheme().getId())
                                .nickname(item.getUser().getNickName())
                                .userProfileImage(item.getUser().getProfileImageUrl())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }
}
