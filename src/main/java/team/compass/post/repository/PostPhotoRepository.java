package team.compass.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.compass.post.entity.PostPhoto;

public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {
}
