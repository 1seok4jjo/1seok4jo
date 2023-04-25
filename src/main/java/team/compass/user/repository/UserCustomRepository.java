package team.compass.user.repository;

import team.compass.post.domain.Post;

import java.util.List;

public interface UserCustomRepository {
    List<Post> findAllByUserPost();
}
