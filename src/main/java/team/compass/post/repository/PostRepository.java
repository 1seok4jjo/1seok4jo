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
    //TODO: 글쓰기 o
    //TODO: 글수정 o
    //TODO: 글삭제 o
    //TODO: 글조회 o -> main page select 삭제
    //TODO: 해당 글 조회 (detail) (눌렀을 때) o
    //TODO: 해당 테마 글 조회 (해당 테마를 누르면 그에 해당되는 글 list 조회) o
    //TODO: 글검색 - DB LIKE 로 해결할지




//    @Query("select p from Post p left join fetch p.likes group by p.id")
//    List<Post> findList(Pageable pageable); // 전체 조회 -> main page -> 쿼리 문제로 인해 좋아요수가 3인 글이 1개로 찍히는 문제

//    @Query("select p from Post p left join fetch p.likes")
//    List<Post> findList(Pageable pageable); // 전체 조회 -> main page (like pk 수로 카운팅하면 좋아요 수가 같이 나옴)
//
//    @Query("select p from Post p left join fetch p.photos pp join fetch pp.photo where p.id in(:id)") // 사진까지 같이 긁어오기
//    List<Post> findListById(@Param(value = "id") List<Integer> id); // photo -> main page


    // 해당 유저 작성 글 조회
    Optional<Page<Post>> findAllByUserId(Integer user_id, Pageable pageable);

    // 해당 유저 좋아요 클릭한 글 조회
    @Query("select l.post from Likes l left join l.post where l.user.id = :user_id")
    Optional<Page<Post>> findAllByUserIdAndLikes(@Param("user_id") Integer id, Pageable pageable);

}


