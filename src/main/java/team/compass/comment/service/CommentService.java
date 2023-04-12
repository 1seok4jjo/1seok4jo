/*
package team.compass.comment.service;


import javax.transaction.Transactional;
import jdk.jshell.spi.ExecutionControl.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.compass.comment.domain.Comment;

import team.compass.comment.repository.CommentRepository;
import team.compass.member.domain.User;
import team.compass.post.entitiy.Post;

@Service
@RequiredArgsConstructor
public class CommentService {
private final CommentRepository commentRepository;


@Transactional
public void registerComment(Comment comment){
    Post post = postRepository.findById(request.getPostId())
    .orElseThrow(()-> new PostException(POST_IS_NOT_FOUND));

    User writer = userRepository.findById(request.getUserId())
    .orElseThrow(()-> new UserException(USER_IS_NOT_FOUND));

    Comment newComment = commentRepository.save(comment);
    }

}
*/
