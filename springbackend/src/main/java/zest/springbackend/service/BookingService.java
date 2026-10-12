package zest.springbackend.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import zest.springbackend.model.Availability;
import zest.springbackend.model.Booking;
import zest.springbackend.model.School;
import zest.springbackend.model.User;
import zest.springbackend.model.enums.BookingStatus;
import zest.springbackend.model.enums.UserRole;
import zest.springbackend.repository.AvailabilityRepository;
import zest.springbackend.repository.BookingRepository;
import zest.springbackend.repository.SchoolRepository;
import zest.springbackend.repository.UserRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final AvailabilityRepository availabilityRepository;
    private final SchedulingService schedulingService;
    private final NotificationService notificationService;

    public BookingService(BookingRepository bookingRepository,
                          SchoolRepository schoolRepository,
                          UserRepository userRepository,
                          AvailabilityRepository availabilityRepository,
                          SchedulingService schedulingService,
                          NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
        this.availabilityRepository = availabilityRepository;
        this.schedulingService = schedulingService;
        this.notificationService = notificationService;
    }

    public Booking createBooking(String date, String startTime, String endTime,
                                String memberSchoolId, String leadTeacherId,
                                String trainerId, String partnerSchoolId) {
        User trainer = userRepository.findById(trainerId)
            .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));
        if (trainer.getRole() != UserRole.TRAINER) {
            throw new IllegalArgumentException("Selected user is not a trainer");
        }

        Booking booking = new Booking(date, startTime, endTime, BookingStatus.SCHEDULED,
            memberSchoolId, leadTeacherId, trainerId, partnerSchoolId);

        Booking saved = bookingRepository.save(booking);

        User leadTeacher = userRepository.findById(leadTeacherId)
            .orElseThrow(() -> new IllegalArgumentException("Lead teacher not found"));

        School memberSchool = schoolRepository.findById(memberSchoolId)
            .orElseThrow(() -> new IllegalArgumentException("Member school not found"));
        School partnerSchool = schoolRepository.findById(partnerSchoolId)
            .orElseThrow(() -> new IllegalArgumentException("Partner school not found"));

        notificationService.sendSessionNotification(
            "SCHEDULED",
            date,
            startTime,
            endTime,
            trainer.getFullName(),
            trainer.getEmail(),
            trainer.getPhoneNumber(),
            trainer.getTimezone(),
            leadTeacher.getFullName(),
            leadTeacher.getEmail(),
            leadTeacher.getTimezone(),
            memberSchool.getName(),
            partnerSchool.getName()
        );

        return saved;
    }

    public Booking cancelBooking(String bookingId, String reason, String callerUserId, boolean isCoordinator) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != BookingStatus.SCHEDULED) {
            throw new IllegalArgumentException("Booking is already cancelled");
        }

        BookingStatus status = isCoordinator
            ? BookingStatus.CANCELLED_BY_SCHOOL
            : schedulingService.determineCancellationStatus(booking.getTrainerId(), booking.getLeadTeacherId(), callerUserId);

        if (status == null) {
            throw new IllegalArgumentException("You are not authorized to cancel this booking");
        }

        booking.setStatus(status);
        booking.setCancelledAt(Instant.now().toString());
        booking.setCancellationReason(reason);

        Booking updated = bookingRepository.save(booking);

        User trainer = userRepository.findById(booking.getTrainerId()).orElse(null);
        User leadTeacher = userRepository.findById(booking.getLeadTeacherId()).orElse(null);
        School memberSchool = schoolRepository.findById(booking.getMemberSchoolId()).orElse(null);
        School partnerSchool = schoolRepository.findById(booking.getPartnerSchoolId()).orElse(null);

        if (trainer != null && leadTeacher != null && memberSchool != null && partnerSchool != null) {
            notificationService.sendSessionNotification(
                "CANCELLED",
                booking.getDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                trainer.getFullName(),
                trainer.getEmail(),
                trainer.getPhoneNumber(),
                trainer.getTimezone(),
                leadTeacher.getFullName(),
                leadTeacher.getEmail(),
                leadTeacher.getTimezone(),
                memberSchool.getName(),
                partnerSchool.getName()
            );
        }

        return updated;
    }

    public List<Booking> findScheduledBookingsForDate(String date) {
        return bookingRepository.findByDateAndStatus(date, BookingStatus.SCHEDULED);
    }

    public Optional<Booking> findById(String bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public List<Availability> getTrainerAvailabilityForDay(String dayName, String schoolId, String date) {
        List<Availability> schoolSlots = availabilityRepository.findByOwnerTypeAndOwnerID(
            zest.springbackend.model.enums.AvailabilityOwnerType.SCHOOL,
            schoolId
        );

        List<Availability> trainerSlots = availabilityRepository.findByOwnerType(
            zest.springbackend.model.enums.AvailabilityOwnerType.TRAINER
        );

        List<Booking> bookings = findScheduledBookingsForDate(date);
        List<Availability> matches = new ArrayList<>();

        for (Availability trainerSlot : trainerSlots) {
            if (trainerSlot.getDayOfWeek().name().equals(dayName)) {
                matches.add(trainerSlot);
            }
        }

        return matches;
    }
}
