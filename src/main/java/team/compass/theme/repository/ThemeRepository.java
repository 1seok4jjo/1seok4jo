package team.compass.theme.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.compass.theme.domain.Theme;

import java.util.List;


public interface ThemeRepository extends JpaRepository<Theme, Integer> {

    @Query("select t from Theme t left join fetch t.post where t.id in(:id)")
    List<Theme> findListByThemeId(@Param(value = "id") Integer id);
}
