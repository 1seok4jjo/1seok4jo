package team.compass.search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.compass.post.domain.Post;
import team.compass.search.dto.SearchRequest;
import team.compass.search.dto.SearchResponse;

import java.util.List;

@Service
public interface SearchService {
    SearchResponse getSearchPostList(SearchRequest parameter);
}
