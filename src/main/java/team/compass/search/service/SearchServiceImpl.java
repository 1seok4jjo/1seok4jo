package team.compass.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import team.compass.post.controller.response.PostResponse;
import team.compass.post.domain.Post;
import team.compass.search.dto.SearchRequest;
import team.compass.search.dto.SearchResponse;
import team.compass.search.repository.SearchRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final int PAGE_ROW_COUNT = 10;
    private final SearchRepository searchRepository;
    @Override
    public SearchResponse getSearchPostList(SearchRequest parameter) {
        Page<Post> postList = getPostList(parameter);

        return SearchResponse.builder()
                .keyword(parameter.getTitle())
                .count(postList.getTotalElements())
                .searchPostList(
                        postList.stream().map(item ->
                            PostResponse.builder()
                                .id(item.getId())
                                .title(item.getTitle())
                                .detail(item.getDetail())
                                .hashtag(item.getHashtag())
                                .location(item.getDetail())
                                .createdAt(item.getCreatedAt())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }

    private Page<Post> getPostList(SearchRequest parameter) {
        Pageable pageable = PageRequest.of(parameter.getPageNum(), PAGE_ROW_COUNT);

        Optional<Page<Post>> optionalPosts = null;

        if(StringUtils.hasText(parameter.getTitle())){
            optionalPosts = searchRepository
                    .findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(parameter.getTitle(), pageable);
        } else if(StringUtils.hasText(parameter.getDetail())) {
            optionalPosts = searchRepository
                    .findAllByDetailContainingIgnoreCaseOrderByCreatedAtDesc(parameter.getDetail(), pageable);
        } else if(StringUtils.hasText(parameter.getHashtag())) {
            optionalPosts = searchRepository
                    .findAllByHashtagContainingIgnoreCaseOrderByCreatedAtDesc(parameter.getHashtag(), pageable);
        }

        Page<Post> postPage = optionalPosts
                .orElseThrow(() -> new RuntimeException("not found post!"));


        return postPage;
    }


}
