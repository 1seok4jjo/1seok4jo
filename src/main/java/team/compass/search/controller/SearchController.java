package team.compass.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.compass.post.domain.Post;
import team.compass.search.dto.SearchRequest;
import team.compass.search.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @GetMapping("/getList")
    public ResponseEntity<?> getSearchPost(
            @RequestBody SearchRequest parameter
            ) {
        List<Post> postList = searchService.getSearchPostList(parameter);
        return ResponseEntity.ok().body(postList);
    }
}
