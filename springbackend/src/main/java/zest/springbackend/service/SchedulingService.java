package zest.springbackend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import zest.springbackend.model.Availability;
import zest.springbackend.model.Booking;
import zest.springbackend.model.enums.BookingStatus;
import zest.springbackend.model.enums.DayOfWeek;

@Service
public class SchedulingService {

    public record IntersectionResult(
        String trainerId,
        String schoolId,
        String overlapStart,
        String overlapEnd,
        String schoolSlotId,
        String trainerSlotId
    ) {
    }

    public List<IntersectionResult> findIntersections(List<Availability> schoolSlots,
                                                     List<Availability> trainerSlots,
                                                     List<Booking> existingBookings) {
        Map<String, List<Booking>> scheduledByTrainer = new HashMap<>();

        for (Booking booking : existingBookings) {
            if (booking.getStatus() != BookingStatus.SCHEDULED) {
                continue;
            }
            scheduledByTrainer.computeIfAbsent(booking.getTrainerId(), key -> new ArrayList<>()).add(booking);
        }

        List<IntersectionResult> results = new ArrayList<>();

        for (Availability school : schoolSlots) {
            int sStart = TimeUtils.timeToMinutes(school.getStartTime());
            int sEnd = TimeUtils.timeToMinutes(school.getEndTime());

            for (Availability trainer : trainerSlots) {
                if (trainer.getDayOfWeek() != school.getDayOfWeek()) {
                    continue;
                }

                int tStart = TimeUtils.timeToMinutes(trainer.getStartTime());
                int tEnd = TimeUtils.timeToMinutes(trainer.getEndTime());

                if (tStart >= sEnd || tEnd <= sStart) {
                    continue;
                }

                int overlapStart = Math.max(sStart, tStart);
                int overlapEnd = Math.min(sEnd, tEnd);

                List<Booking> trainerBookings = scheduledByTrainer.getOrDefault(trainer.getOwnerID(), List.of());
                boolean hasConflict = trainerBookings.stream().anyMatch(booking -> {
                    int bStart = TimeUtils.timeToMinutes(booking.getStartTime());
                    int bEnd = TimeUtils.timeToMinutes(booking.getEndTime());
                    return bStart < overlapEnd && bEnd > overlapStart;
                });

                if (!hasConflict) {
                    results.add(new IntersectionResult(
                        trainer.getOwnerID(),
                        school.getOwnerID(),
                        TimeUtils.minutesToTime(overlapStart),
                        TimeUtils.minutesToTime(overlapEnd),
                        school.getId(),
                        trainer.getId()
                    ));
                }
            }
        }

        return results;
    }

    public BookingStatus determineCancellationStatus(String trainerId, String leadTeacherId, String callerUserId) {
        if (trainerId.equals(callerUserId)) {
            return BookingStatus.CANCELLED_BY_TRAINER;
        }
        if (leadTeacherId.equals(callerUserId)) {
            return BookingStatus.CANCELLED_BY_SCHOOL;
        }
        return null;
    }

    public static String toDayOfWeekValue(String date) {
        return DayOfWeek.values()[TimeUtils.dateToDayOfWeek(date).getValue() - 1].name();
    }
}
