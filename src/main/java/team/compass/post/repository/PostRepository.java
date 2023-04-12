package team.compass.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.compass.post.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    //TODO: 글쓰기 o
    //TODO: 글수정 o
    //TODO: 글삭제 o
    //TODO: 글조회 o
    //TODO: 해당 글 조회 (detail) (눌렀을 때) o
    //TODO: 해당 테마 글 조회 (해당 테마를 누르면 그에 해당되는 글 list 조회)
    //TODO: 글검색 - DB LIKE 로 해결할지

    @Query("select p from Post p left join fetch p.photos where p.id = :id")
    Optional<Post> findById(@Param(value = "id") Integer id); // 단건 조회 (사진)

    @Query("select p from Post p left join fetch  p.likes left join fetch p.user where p.id = :id")
    Optional<Post> findWithLikeById(@Param(value = "id") Integer id); // 단건 조회 (좋아요 수 같이)

    @Query("select p from Post p left join fetch p.likes group by p.id")
    List<Post> findList(); // 전체 조회 -> main page

    @Query("select p from Post p left join fetch p.photos pp join fetch pp.photo where p.id in(:id)") // 사진까지 같이 긁어오기
    List<Post> findListById(@Param(value = "id") List<Integer> id); // photo -> main page

}


/*
    TODO : 해당 글 조회
    쿼리 한 번에 vs 나눠서 (글, 좋아요 + 사진)

    쿼리 한 번에 ?

    SELECT *, (select count(*) from likes l where l.post_id =2 group by l.post_id) as likesCount
    FROM post p
    left join  post_photo pp on p.post_id =pp.post_id
    left join photo p2 on pp.photo_id = p2.photo_id
    where p.post_id = {};


    쿼리 나눠서 하기
    <- 글, 좋아요 -> (해당 글이 없으면 아예 데이터 출력 X)
    select *, count(likes_id)
    from post p
    left join likes l on p.post_id = l.post_id
    where p.post_id = {}
    group by l.post_id;

    <- 사진 ->
    select * from
    post_photo pp
    left join photo p on pp.photo_id = p.photo_id
    where pp.post_id = {}
 */
