package team.compass.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.compass.post.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}