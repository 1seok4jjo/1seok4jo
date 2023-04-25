package team.compass.comment.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.compass.comment.dto.CommentRequest;
import team.compass.comment.dto.CommentResponse;
import team.compass.comment.service.CommentService;
import team.compass.common.utils.ResponseUtils;


@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comment")
   public ResponseEntity<Object> commentCreate(@PathVariable Integer postId,
       @Valid @RequestBody CommentRequest commentRequest) {

        commentRequest.setPostId(postId);
        CommentResponse commentResponse = commentService.registerComment(commentRequest);

        if (commentResponse != null) {
            return ResponseUtils.ok("댓글을 작성하였습니다.", "ok");
        } else {
            return ResponseUtils.notFound("댓글 작성에 실패하였습니다.");
        }
    }
    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<Object> getCommentListByPostId(@PathVariable Integer postId){

        List<CommentResponse> response = commentService.getCommentListByPostId(postId);

        if (response != null) {
            return ResponseUtils.ok("댓글목록 조회에 성공하였습니다.", response);
        } else {
            return ResponseUtils.notFound("댓글목록 조회에 실패하였습니다.");
        }
    }

   @PutMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<Object> commentUpdate(@PathVariable Integer postId,
        @PathVariable Integer commentId,
        @RequestBody CommentRequest commentRequest) {


       commentRequest.setPostId(postId);
        CommentResponse commentResponse = commentService.updateComment(commentId, commentRequest);

       if (commentResponse != null) {
           return ResponseUtils.ok("댓글 수정에 성공하였습니다.", commentResponse);
       } else {
           return ResponseUtils.notFound("댓글 수정에 실패하였습니다.");
       }
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Integer commentId,CommentRequest commentRequest) {
        boolean isDeleted = commentService.deleteComment(commentId, commentRequest);
       
        if (isDeleted) {
            return ResponseUtils.ok("댓글을 삭제하였습니다.", null);
        } else {
            return ResponseUtils.notFound("댓글 삭제에 실패하였습니다.");
        }
    }
    
    
    }

