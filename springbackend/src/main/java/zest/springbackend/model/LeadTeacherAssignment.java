package zest.springbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "lead_teacher_assignments")
public class LeadTeacherAssignment {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String schoolId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String assignedAt;

    private String removedAt;

    @Column(nullable = false)
    private boolean isActive;

    public LeadTeacherAssignment() {
    }

    public LeadTeacherAssignment(String schoolId, String userId, String assignedAt, boolean isActive) {
        this.schoolId = schoolId;
        this.userId = userId;
        this.assignedAt = assignedAt;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(String assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(String removedAt) {
        this.removedAt = removedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
