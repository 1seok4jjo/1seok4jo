package team.compass.comment.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.compass.comment.domain.Comment;

import team.compass.post.domain.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    // Post �� �� ���� ��� �� Ȯ��
    Long countByPostId(Integer postId);


    List<Comment> findAllByPostIdAndPost(Integer postId, Post post);
}
