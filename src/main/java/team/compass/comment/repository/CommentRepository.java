package team.compass.comment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.compass.comment.domain.Comment;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    // Post �� �� ���� ��� �� Ȯ��
    Long countByPostId(Integer postId);
}
