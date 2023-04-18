package team.compass.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.compass.post.domain.Post;
import team.compass.search.dto.SearchRequest;
import team.compass.search.repository.SearchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final int PAGE_ROW_COUNT = 10;
    private final SearchRepository searchRepository;
    @Override
    public List<Post> getSearchPostList(SearchRequest parameter) {
        int pageNum = 1;

        int startPageNum = Integer.parseInt(parameter.getPageNum());

        int startRowNum = 0 + (pageNum - 1) * PAGE_ROW_COUNT;
        int endRowNum = pageNum * PAGE_ROW_COUNT;




        List<Post> postList = searchRepository.findAllByTitleContaining(parameter.getTitle())
                .orElseThrow(() -> new RuntimeException("해당 포스트가 없습니다."));

        return postList;
    }

}
