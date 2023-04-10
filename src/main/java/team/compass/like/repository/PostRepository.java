package team.compass.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.compass.post.entity.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findById(int id);
}
