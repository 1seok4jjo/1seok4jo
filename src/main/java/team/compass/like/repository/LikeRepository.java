package team.compass.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import team.compass.like.domain.Likes;
import team.compass.user.domain.User;
import team.compass.post.domain.Post;


import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Integer> {

    Optional<Likes> findByPostAndUser(Post post, User user);
}
