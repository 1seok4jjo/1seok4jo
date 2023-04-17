package team.compass.comment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.compass.comment.domain.Comment;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    // Post 상세 글 보기 댓글 수 확인
    Long countByPostId(Integer postId);
}
