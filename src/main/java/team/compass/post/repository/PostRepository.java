package team.compass.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.compass.post.domain.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    // 해당 유저 작성 글 조회
    Optional<List<Post>> findAllByUser_Id(Integer id);

    // 해당 유저 좋아요 클릭한 글 조회
    @Query("select l.post from Likes l left join l.post where l.user.id = :user_id")
    Optional<List<Post>> findAllByUserIdAndLikes(@Param("user_id") Integer id);

}


