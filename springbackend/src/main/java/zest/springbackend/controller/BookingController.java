package zest.springbackend.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zest.springbackend.dto.ApiResponse;
import zest.springbackend.model.Booking;
import zest.springbackend.service.BookingService;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    public ApiResponse<Booking> createBooking(@RequestBody Booking request) {
        Booking booking = bookingService.createBooking(
            request.getDate(),
            request.getStartTime(),
            request.getEndTime(),
            request.getMemberSchoolId(),
            request.getLeadTeacherId(),
            request.getTrainerId(),
            request.getPartnerSchoolId()
        );
        return ApiResponse.ok("Booking created", booking);
    }

    @PostMapping("/bookings/{bookingId}/cancel")
    public ApiResponse<Booking> cancelBooking(@PathVariable String bookingId,
                                             @RequestParam String userId,
                                             @RequestParam(defaultValue = "false") boolean isCoordinator,
                                             @RequestBody(required = false) Booking payload) {
        String reason = payload != null ? payload.getCancellationReason() : null;
        Booking updated = bookingService.cancelBooking(bookingId, reason, userId, isCoordinator);
        return ApiResponse.ok("Booking cancelled", updated);
    }
}
