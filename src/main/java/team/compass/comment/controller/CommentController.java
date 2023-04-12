package team.compass.comment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CommentController {

    //private final CommentService commentService;

    @PostMapping("/postId/comment")
    public ResponseEntity<Long> createComment(){
        return null;
    }

}
