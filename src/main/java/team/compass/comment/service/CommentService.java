package team.compass.comment.service;


import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.compass.comment.domain.Comment;
import team.compass.comment.dto.CommentRequest;
import team.compass.comment.dto.CommentResponse;
import team.compass.comment.repository.CommentRepository;
import team.compass.post.domain.Post;
import team.compass.post.repository.PostRepository;
import team.compass.user.domain.User;
import team.compass.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //댓글 생성
    @Transactional
    public CommentResponse registerComment(CommentRequest request) {
        Post post = postRepository.findById(request.getPostId())
            .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다"));

        User writer = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다"));

        Comment newComment = commentRepository.save(request.requestComment(post, writer));

        return CommentResponse.responseComment(newComment, writer);
    }
    //댓글조회
    public List<CommentResponse> getCommentListByPostId(Integer postId){

        List<Comment> commentList = commentRepository.findAllByPostId(postId);

       return commentList.stream()
           .map(CommentResponse::fromEntity)
           .collect(Collectors.toList());
    }

//댓글수정
    public CommentResponse updateComment(Integer commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("댓글은 댓글을 쓴 사람만 수정 할 수 있습니다.");
        }
        comment.updateContent(request.getContent());
        commentRepository.save(comment);

        return CommentResponse.fromEntity(comment);
    }

//댓글 삭제
    public void deleteComment(Integer commentId) {

        commentRepository.findById(commentId).ifPresent(commentRepository::delete);
    }
}

