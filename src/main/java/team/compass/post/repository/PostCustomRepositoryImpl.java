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

        if (lastId == null) { // ���� lastId �� null �� ���
            query = entityManager.createQuery(first, Post.class) // ù��° ����
                    .setParameter("theme", theme); // �ش� �׸� ���̵� �ֱ�
        } else {
            query = entityManager
                    .createQuery(paging, Post.class) // lastId �� ���� ����ε�
                    .setParameter("theme", theme) // �ش� �׸� ���̵� �ֱ�
                    .setParameter("lastId", lastId); // lastId �������� �̰ͺ��� ���� �͵� list, orderBy desc
        }

        return query
                .setMaxResults(10) // 5���� �̾ƿɴϴ�.
                .getResultList(); // list
    }
}