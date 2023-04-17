package team.compass.search.service;

import org.springframework.stereotype.Service;
import team.compass.post.domain.Post;

import java.util.List;

@Service
public interface SearchService {
    List<Post> getTitleContainList(String title);
}
