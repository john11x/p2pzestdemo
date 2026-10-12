package zest.springbackend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import zest.springbackend.dto.CreateSchoolAvailabilityRequest;
import zest.springbackend.model.Availability;
import zest.springbackend.model.School;
import zest.springbackend.model.User;
import zest.springbackend.model.enums.AvailabilityOwnerType;
import zest.springbackend.model.enums.DayOfWeek;
import zest.springbackend.model.enums.UserRole;
import zest.springbackend.repository.AvailabilityRepository;
import zest.springbackend.repository.SchoolRepository;
import zest.springbackend.repository.UserRepository;

@Service
public class SchoolAvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    public SchoolAvailabilityService(AvailabilityRepository availabilityRepository,
                                    SchoolRepository schoolRepository,
                                    UserRepository userRepository) {
        this.availabilityRepository = availabilityRepository;
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
    }

    public Availability createSchoolAvailability(CreateSchoolAvailabilityRequest request, String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));

        if (user.getRole() != UserRole.LEAD_TEACHER) {
            throw new IllegalArgumentException("Only lead teachers can create school availability");
        }

        School school = schoolRepository.findById(request.schoolId())
            .orElseThrow(() -> new IllegalArgumentException("School not found"));

        if (!request.schoolId().equals(user.getSchoolId())) {
            throw new IllegalArgumentException("You can only add availability for your own school");
        }

        if (request.startDate() != null && !request.startDate().isBlank()) {
            LocalDate today = LocalDate.now();
            if (LocalDate.parse(request.startDate()).isBefore(today)) {
                throw new IllegalArgumentException("Cannot set availability for past dates");
            }
        }

        List<Availability> existing = availabilityRepository.findByOwnerTypeAndOwnerID(
            AvailabilityOwnerType.SCHOOL,
            request.schoolId()
        );

        for (Availability slot : existing) {
            if (TimeUtils.slotsOverlap(
                slot.getDayOfWeek().name(),
                slot.getStartTime(),
                slot.getEndTime(),
                request.dayOfWeek().name(),
                request.startTime(),
                request.endTime()
            )) {
                throw new IllegalArgumentException("This availability overlaps with an existing slot");
            }
        }

        Availability availability = new Availability(
            request.schoolId(),
            AvailabilityOwnerType.SCHOOL,
            request.dayOfWeek(),
            request.startTime(),
            request.endTime(),
            request.isRecurring(),
            request.timezone(),
            request.startDate(),
            request.endDate()
        );

        return availabilityRepository.save(availability);
    }

    public void deleteAvailability(String id) {
        availabilityRepository.deleteById(id);
    }

    public List<Availability> findBySchool(String schoolId) {
        return availabilityRepository.findByOwnerTypeAndOwnerID(AvailabilityOwnerType.SCHOOL, schoolId);
    }

    public Optional<Availability> findById(String id) {
        return availabilityRepository.findById(id);
    }
}
