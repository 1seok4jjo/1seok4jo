package team.compass.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.compass.post.domain.Post;
import team.compass.search.repository.SearchRepository;

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
}
