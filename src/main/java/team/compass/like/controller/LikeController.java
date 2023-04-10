package team.compass.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.compass.like.dto.LikeDto;
import team.compass.like.service.LikeService;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

//    @PostMapping
//    public HttpResponseEntity.ResponseResult<?> add(@RequestBody @Valid LikeDto likeDto) throws Exception {
//        likeService.addLike(likeDto);
//        return success(null);
//    }
//
//    @DeleteMapping
//    public HttpResponseEntity.ResponseResult<?> cancel(@RequestBody @Valid LikeDto likeDto) throws Exception{
//        likeService.cancelLike(likeDto);
//        return success(null);
//    }

}