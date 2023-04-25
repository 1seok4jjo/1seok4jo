package team.compass.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.compass.post.domain.Post;

import java.util.Optional;

@Repository
public interface SearchRepository extends JpaRepository<Post, Integer> {
    // ignoreCase 대소문자 구분 x
    // 제목
    Optional<Page<Post>> findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title, Pageable pageable);
    // 내용
    Optional<Page<Post>> findAllByDetailContainingIgnoreCaseOrderByCreatedAtDesc(String detail, Pageable pageable);
    // 해시태그
    Optional<Page<Post>> findAllByHashtagContainingIgnoreCaseOrderByCreatedAtDesc(String hashTag, Pageable pageable);
}
