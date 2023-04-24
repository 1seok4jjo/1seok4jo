package team.compass.like.repository;

import team.compass.post.domain.Post;

import java.util.List;

public interface LikeCustomRepository {
    List<Post> findAllByUser(Integer id);
}
