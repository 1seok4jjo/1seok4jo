package team.compass.post.repository;

import team.compass.post.domain.Post;

import java.util.List;

public interface PostCustomRepository  {
    List<Post> findByTheme(Integer theme, Integer lastId);
}