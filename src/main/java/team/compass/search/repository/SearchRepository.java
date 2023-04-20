package team.compass.search.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import team.compass.post.domain.Post;

import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<Post, Integer> {
    // 제목
    Optional<List<Post>> findAllByTitleContaining(@Param("title") String title);

    //내용
    Optional<List<Post>> findAllByDetailContaining(@Param("detail") String detail);

}
