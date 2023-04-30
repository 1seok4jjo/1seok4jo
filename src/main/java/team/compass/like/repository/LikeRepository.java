package team.compass.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import team.compass.like.domain.Likes;
import team.compass.post.domain.Post;
import team.compass.user.domain.User;


import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Integer> {

    Optional<Likes> findAllByPost_IdAndUser_Id(Integer post_id,
                                               Integer user_id);

    Optional<Likes> findByUserAndPost(User user, Post post);
}
