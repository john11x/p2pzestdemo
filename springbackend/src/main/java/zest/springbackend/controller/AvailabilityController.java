package zest.springbackend.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zest.springbackend.dto.ApiResponse;
import zest.springbackend.dto.CreateSchoolAvailabilityRequest;
import zest.springbackend.dto.FindAvailableTrainersRequest;
import zest.springbackend.model.Availability;
import zest.springbackend.model.Booking;
import zest.springbackend.model.enums.AvailabilityOwnerType;
import zest.springbackend.model.enums.DayOfWeek;
import zest.springbackend.repository.AvailabilityRepository;
import zest.springbackend.repository.BookingRepository;
import zest.springbackend.repository.UserRepository;
import zest.springbackend.service.SchoolAvailabilityService;
import zest.springbackend.service.SchedulingService;

@RestController
@RequestMapping("/api")
public class AvailabilityController {

    private final SchoolAvailabilityService schoolAvailabilityService;
    private final AvailabilityRepository availabilityRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SchedulingService schedulingService;

    public AvailabilityController(SchoolAvailabilityService schoolAvailabilityService,
                                 AvailabilityRepository availabilityRepository,
                                 BookingRepository bookingRepository,
                                 UserRepository userRepository,
                                 SchedulingService schedulingService) {
        this.schoolAvailabilityService = schoolAvailabilityService;
        this.availabilityRepository = availabilityRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.schedulingService = schedulingService;
    }

    @PostMapping("/availability")
    public ApiResponse<Availability> createSchoolAvailability(@Valid @RequestBody CreateSchoolAvailabilityRequest request,
                                                             @RequestParam String userId) {
        Availability availability = schoolAvailabilityService.createSchoolAvailability(request, userId);
        return ApiResponse.ok("Availability created", availability);
    }

    @GetMapping("/availability/school/{schoolId}")
    public ApiResponse<List<Availability>> getSchoolAvailability(@PathVariable String schoolId) {
        return ApiResponse.ok("School availability loaded", schoolAvailabilityService.findBySchool(schoolId));
    }

    @DeleteMapping("/availability/{id}")
    public ApiResponse<String> deleteSchoolAvailability(@PathVariable String id) {
        schoolAvailabilityService.deleteAvailability(id);
        return ApiResponse.ok("Availability deleted", id);
    }

    @GetMapping("/availability/find-trainers")
    public ApiResponse<List<SchedulingService.IntersectionResult>> findAvailableTrainers(@Valid FindAvailableTrainersRequest request) {
        List<Availability> schoolSlots = availabilityRepository.findByOwnerTypeAndOwnerID(
            AvailabilityOwnerType.SCHOOL,
            request.schoolId()
        );
        List<Availability> trainerSlots = availabilityRepository.findByOwnerType(AvailabilityOwnerType.TRAINER);
        List<Booking> scheduledBookings = bookingRepository.findByDateAndStatus(request.date(), zest.springbackend.model.enums.BookingStatus.SCHEDULED);

        var matches = schedulingService.findIntersections(schoolSlots, trainerSlots, scheduledBookings);
        return ApiResponse.ok("Available trainer slots loaded", matches);
    }
}
