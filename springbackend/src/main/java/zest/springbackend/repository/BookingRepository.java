package zest.springbackend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zest.springbackend.model.Booking;
import zest.springbackend.model.enums.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByDateAndStatus(String date, BookingStatus status);
    List<Booking> findByTrainerIdAndStatus(String trainerId, BookingStatus status);
    List<Booking> findByLeadTeacherIdAndStatus(String leadTeacherId, BookingStatus status);
}
