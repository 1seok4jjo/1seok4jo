package team.compass.photo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.compass.photo.domain.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

}
