package team.compass.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.compass.user.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
