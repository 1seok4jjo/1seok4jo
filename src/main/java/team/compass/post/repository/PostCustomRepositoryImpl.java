package team.compass.post.repository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.compass.post.domain.Post;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<Post> findByTheme(Integer theme, Integer lastId) {

        String first = "select p from Post p  where p.theme.id =:theme order by p.id desc";

        String paging = "select p from Post p where p.theme.id =:theme and p.id < :lastId order by p.id desc ";

        TypedQuery<Post> query=null;

        if (lastId == null) {
            query = entityManager.createQuery(first, Post.class)
                    .setParameter("theme", theme);
        } else {
            query = entityManager
                    .createQuery(paging, Post.class)
                    .setParameter("theme", theme)
                    .setParameter("lastId", lastId); // lastId 기준으로 list, orderBy desc
        }

        return query
                .setMaxResults(10) // 10개 제한
                .getResultList(); // list
    }

    /**
     *  javax.persistence.fetchgraph 말고도 loadgraph 가 존재한다. 해당 속성은 엔티티 그래프에 선택한
     *  속성뿐만 아니라 글로벌 fetch 모드가 EAGER로 설정된 연관관계도 포함해 조회하게 된다.
     *  여기서 사용한 fetchgraph는 선택한 속성만 조회하는 모드이다.
     *  엔티티 그래프는 항상 ROOT(Post) 에서 시작할 것.
     *  이미 로딩된 엔티티는 적용되지 않는다. 하지만 아직 초기화 되지 않은 프록시엔 적용 가능!
     */
    @Override
    public Optional<Post> findWithLikeById(@Param(value = "id") Integer id){

        EntityGraph<Post> entityGraph = entityManager.createEntityGraph(Post.class);
        entityGraph.addAttributeNodes("photos","user");

        Subgraph<Object> photos = entityGraph.addSubgraph("photos"); // 서브그래프 더해주기
        photos.addAttributeNodes("photo");

        Map<String,Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(entityManager.find(Post.class, id, hints));
    }
}