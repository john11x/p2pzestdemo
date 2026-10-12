package zest.springbackend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zest.springbackend.model.LeadTeacherAssignment;

@Repository
public interface LeadTeacherAssignmentRepository extends JpaRepository<LeadTeacherAssignment, String> {
    List<LeadTeacherAssignment> findByIsActiveTrue();
    List<LeadTeacherAssignment> findBySchoolIdAndIsActiveTrue(String schoolId);
}
