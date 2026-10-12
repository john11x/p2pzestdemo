package zest.springbackend.service;

import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Service;
import zest.springbackend.dto.CreateUserRequest;
import zest.springbackend.model.LeadTeacherAssignment;
import zest.springbackend.model.School;
import zest.springbackend.model.User;
import zest.springbackend.model.enums.SchoolType;
import zest.springbackend.model.enums.UserRole;
import zest.springbackend.repository.LeadTeacherAssignmentRepository;
import zest.springbackend.repository.SchoolRepository;
import zest.springbackend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final LeadTeacherAssignmentRepository leadTeacherAssignmentRepository;

    public UserService(UserRepository userRepository,
                       SchoolRepository schoolRepository,
                       LeadTeacherAssignmentRepository leadTeacherAssignmentRepository) {
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.leadTeacherAssignmentRepository = leadTeacherAssignmentRepository;
    }

    public User createUser(CreateUserRequest request) {
        if (!request.role().equals(UserRole.LEAD_TEACHER) && !request.role().equals(UserRole.TRAINER) && !request.role().equals(UserRole.COORDINATOR)) {
            throw new IllegalArgumentException("Invalid role provided");
        }

        School school = null;
        if (request.schoolId() != null && !request.schoolId().isBlank()) {
            school = schoolRepository.findById(request.schoolId())
                .orElseThrow(() -> new IllegalArgumentException("School not found: " + request.schoolId()));

            if (request.role() == UserRole.LEAD_TEACHER && school.getType() != SchoolType.MEMBER) {
                throw new IllegalArgumentException("Lead teachers must belong to a MEMBER school");
            }
            if (request.role() == UserRole.TRAINER && school.getType() != SchoolType.PARTNER) {
                throw new IllegalArgumentException("Trainers must belong to a PARTNER school");
            }
        }

        User user = new User(
            request.fullName().trim(),
            request.email().trim().toLowerCase(),
            request.phoneNumber(),
            request.role(),
            request.timezone(),
            request.schoolId() == null || request.schoolId().isBlank() ? null : request.schoolId()
        );

        User savedUser = userRepository.save(user);

        if (request.role() == UserRole.LEAD_TEACHER && request.schoolId() != null && !request.schoolId().isBlank()) {
            LeadTeacherAssignment assignment = new LeadTeacherAssignment(
                request.schoolId(),
                savedUser.getId(),
                Instant.now().toString(),
                true
            );
            leadTeacherAssignmentRepository.save(assignment);
        }

        return savedUser;
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
