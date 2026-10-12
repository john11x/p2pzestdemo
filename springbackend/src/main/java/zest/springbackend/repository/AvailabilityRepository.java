package zest.springbackend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zest.springbackend.model.Availability;
import zest.springbackend.model.enums.AvailabilityOwnerType;
import zest.springbackend.model.enums.DayOfWeek;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, String> {
    List<Availability> findByOwnerTypeAndDayOfWeekAndOwnerID(AvailabilityOwnerType ownerType, DayOfWeek dayOfWeek, String ownerID);
    List<Availability> findByOwnerTypeAndOwnerID(AvailabilityOwnerType ownerType, String ownerID);
    List<Availability> findByOwnerType(AvailabilityOwnerType ownerType);
    List<Availability> findByDayOfWeekAndOwnerType(DayOfWeek dayOfWeek, AvailabilityOwnerType ownerType);
}
