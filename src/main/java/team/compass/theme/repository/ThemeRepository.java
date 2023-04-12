package team.compass.theme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.compass.theme.domain.Theme;


public interface ThemeRepository extends JpaRepository<Theme, Integer> {
}
