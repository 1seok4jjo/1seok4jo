package team.compass.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.compass.post.domain.Post;
import team.compass.post.dto.PostDto;
import team.compass.search.repository.SearchRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;

    @Override
    public List<Post> getTitleContainList(String title) {
        List<Post> postList = searchRepository.findAllByTitleContaining(title)
                .orElseThrow(() -> new RuntimeException("해당 포스트가 없습니다."));

        return postList;
    }

    @Override
    public List<PostDto> getDetailContainList(String detail) {

        List<Post> postList = searchRepository.findAllByDetailContaining(detail)
                .orElseThrow(() -> new RuntimeException("해당 포스트가 없습니다."));
        List<PostDto> postDtoList = new ArrayList<>();

        if (postList.isEmpty()){
            return postDtoList;
        }

        for (Post post : postList) {
            postDtoList.add(this.entityToDto(post));
        }

        return postDtoList;
    }

    private PostDto entityToDto(Post post) {

        return PostDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
//                .likeCount()
                .location(post.getLocation())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .build();
    }
}
