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
    public SearchResponse getSearchPostList(String type, String text) {
        Page<Post> postList = getPostList(type, text);

        return SearchResponse.builder()
                .keyword(text)
                .count(postList.getTotalElements())
                .searchPostList(
                        postList.stream().map(item ->
                            PostResponse.builder()
                                .id(item.getId())
                                .title(item.getTitle())
                                .detail(item.getDetail())
                                .hashtag(item.getHashtag())
                                .location(item.getLocation())
                                .createdAt(item.getCreatedAt())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }

    private Page<Post> getPostList(String type, String text) {
        Pageable pageable = PageRequest.of(0, PAGE_ROW_COUNT);

        Optional<Page<Post>> optionalPosts = null;

        System.out.println(type);

        if(type.equals("title")){
            optionalPosts = searchRepository
                    .findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(text, pageable);
        } else if(type.equals("detail")) {
            optionalPosts = searchRepository
                    .findAllByDetailContainingIgnoreCaseOrderByCreatedAtDesc(text, pageable);
        } else if(type.equals("hashtag")) {
            optionalPosts = searchRepository
                    .findAllByHashtagContainingIgnoreCaseOrderByCreatedAtDesc(text, pageable);
        }

        Page<Post> postPage = optionalPosts
                .orElseThrow(() -> new RuntimeException("not found post!"));


        return postPage;
    }


}
