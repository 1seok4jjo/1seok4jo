package team.compass.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import team.compass.post.entity.Likes;
import team.compass.post.entity.Post;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Integer> {

    Optional<Likes> findByPostAndUser(Post post, User user);
}
