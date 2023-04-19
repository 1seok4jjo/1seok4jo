package team.compass.search.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.compass.post.domain.Post;
import team.compass.search.dto.SearchRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchRepository extends JpaRepository<Post, Integer> {
    // 제목
    Optional<Page<Post>> findAllByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable);
    // 내용
    Optional<Page<Post>> findAllByDetailContainingOrderByCreatedAtDesc(String detail, Pageable pageable);
    // 해시태그
    Optional<Page<Post>> findAllByHashtagContainingOrderByCreatedAtDesc(String hashTag, Pageable pageable);

//    Optional<Page<Post>> findAllByTitleContainingOrDetailContainingOrHashtagContainingOrderByCreatedAtDesc(String keyword, String keyword2, String keyword3,  Pageable pageable);
}
