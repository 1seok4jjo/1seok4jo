package team.compass.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.compass.post.domain.Post;
import team.compass.post.dto.PostDto;
import team.compass.search.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/getList")
    public ResponseEntity<?> getTitleContainsPost(
            @RequestParam String title
    ) {
        List<Post> postList = searchService.getTitleContainList(title);
        System.out.println(title);
        return ResponseEntity.ok().body(postList);
    }

    @GetMapping("/getList/search")
    public ResponseEntity<?> getDetailContainsPost(
            @RequestParam(value = "detail") String detail, Model model
    ){
        List<PostDto> postDtoList = searchService.getDetailContainList(detail);
        model.addAttribute("detail", detail);
        model.addAttribute("postDtoList", postDtoList);

        return ResponseEntity.ok().body(postDtoList);
    }
}
