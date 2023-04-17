package team.compass.comment.controller;


import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.compass.comment.dto.CommentRequest;
import team.compass.comment.dto.CommentResponse;
import team.compass.comment.service.CommentService;


@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentResponse> commentCreate(@PathVariable Integer postId,Integer userId,
        @Valid @RequestBody CommentRequest commentRequest) {

        commentRequest.setPostId(postId);
        CommentResponse commentResponse = commentService.registerComment(commentRequest,postId,userId);

        return ResponseEntity.ok(commentResponse);
    }
}
    /*
    @PutMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<CommentResponse> commentUpdate(@PathVariable Integer postId,
        @PathVariable Integer commentId,
        @RequestBody CommentRequest commentRequest) {

        commentRequest.setPostId(postId);
        CommentResponse commentResponse = commentService.updateComment(commentId, commentRequest);

        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {

        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }
}

     */
