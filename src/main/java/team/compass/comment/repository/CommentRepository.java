package team.compass.comment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.compass.comment.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

}
