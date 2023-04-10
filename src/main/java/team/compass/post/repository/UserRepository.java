package team.compass.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.compass.post.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

}
