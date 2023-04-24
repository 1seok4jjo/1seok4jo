package team.compass.like.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.compass.post.domain.Post;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeCustomRepositoryImpl implements LikeCustomRepository {
    private final EntityManager entityManager;
    @Override
    public List<Post> findAllByUser(Integer id) {

        return null;
    }
}
