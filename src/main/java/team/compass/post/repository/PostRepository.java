package team.compass.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.compass.post.entity.Post;

import java.util.List;
import java.util.Optional;

//TODO: 글쓰기
//TODO: 글수정
//TODO: 글삭제
//TODO: 글조회
//TODO: 해당 글 조회 (detail)

//TODO: 글목록조회 - ?
//TODO: 글좋아요 - ? 은수님?
//TODO: 글좋아요취소 - ? 은수님..?
//TODO: 글검색 - LIKE 문 쓸지 고민
//TODO: 어떤 테마 누르면 해당 테마에 해당되는 글 목록

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {


    @Query("select p from Post p left join fetch p.photos where p.id = :id")
    Optional<Post> findById(@Param(value = "id") Long id); // 단건 조회

    @Query("select p from Post p left join fetch p.likes group by p.id")
    List<Post> findList(); // 전체 조회

    @Query("select p from Post p left join fetch p.photos pp join fetch pp.photo where p.id in(:id)") // 사진까지 같이 긁어오기
    List<Post> findListById(@Param(value = "id") List<Long> id); // photo
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

