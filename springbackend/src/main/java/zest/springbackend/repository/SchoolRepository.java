package zest.springbackend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zest.springbackend.model.School;
import zest.springbackend.model.enums.SchoolType;

@Repository
public interface SchoolRepository extends JpaRepository<School, String> {
    List<School> findByType(SchoolType type);
}
