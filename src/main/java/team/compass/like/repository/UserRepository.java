package team.compass.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.compass.member.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(int id);
}
