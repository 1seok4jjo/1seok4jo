//package team.compass.like.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import team.compass.like.dto.LikeDto;
//import team.compass.like.service.LikeService;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/post/like")
//public class LikeController {
//
//    private final LikeService likeService;
//
//    @PostMapping
//    public void addLike(@RequestBody LikeDto likeDto) throws Exception{
//        likeService.add(likeDto);
//    }
//
//    @DeleteMapping("/delete")
//    public void cancelLike(@RequestBody LikeDto likeDto) throws Exception {
//        likeService.cancel(likeDto);
//    }
//}