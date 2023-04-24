package team.compass.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.compass.like.dto.LikeDto;
import team.compass.like.service.LikeService;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/post/like")
    public void addLike(@RequestBody LikeDto likeDto, @PathVariable Integer id){
        likeService.saveLike(likeDto);
    }
}