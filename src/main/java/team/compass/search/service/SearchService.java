package team.compass.search.service;

import org.springframework.stereotype.Service;
import team.compass.post.domain.Post;
import team.compass.post.dto.PostDto;

import java.util.List;

@Service
public interface SearchService {
    List<Post> getTitleContainList(String title);

    List<PostDto> getDetailContainList(String detail);
}
