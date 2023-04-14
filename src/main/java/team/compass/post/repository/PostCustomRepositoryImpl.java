package team.compass.post.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.compass.post.domain.Post;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<Post> findByTheme(Integer theme, Integer lastId) {

        String first = "select p from Post p  where p.theme.id =:theme order by p.id desc";

        String paging = "select p from Post p where p.theme.id =:theme and p.id < :lastId order by p.id desc ";

        TypedQuery<Post> query=null;

        if (lastId == null) { // 만약 lastId 가 null 일 경우
            query = entityManager.createQuery(first, Post.class) // 첫번째 쿼리
                    .setParameter("theme", theme); // 해당 테마 아이디 넣기
        } else {
            query = entityManager
                    .createQuery(paging, Post.class) // lastId 가 있을 경우인데
                    .setParameter("theme", theme) // 해당 테마 아이디 넣기
                    .setParameter("lastId", lastId); // lastId 기준으로 이것보다 작은 것들 list, orderBy desc
        }

        return query
                .setMaxResults(10) // 5개를 뽑아옵니다.
                .getResultList(); // list
    }
}