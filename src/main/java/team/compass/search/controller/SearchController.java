package team.compass.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.compass.common.utils.ResponseUtils;
import team.compass.post.domain.Post;
import team.compass.search.dto.SearchRequest;
import team.compass.search.dto.SearchResponse;
import team.compass.search.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @GetMapping("/getList/{type}/{text}")
    public ResponseEntity<?> getSearchPost(
            @RequestBody SearchRequest parameter,
            @PathVariable String type,
            @PathVariable String text
    ) {
        SearchResponse postList = searchService.getSearchPostList(parameter, type, text);

        return ResponseUtils.ok("게시글 검색 조회에 성공하였습니다.", postList);
    }
}
