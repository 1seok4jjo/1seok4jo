package team.compass.post.repository;

import org.springframework.data.repository.query.Param;
import team.compass.post.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostCustomRepository  {
    List<Post> findByTheme(Integer theme, Integer lastId);
    Optional<Post> findWithLikeById(@Param(value = "id") Integer id);
}