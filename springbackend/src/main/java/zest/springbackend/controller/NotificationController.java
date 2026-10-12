package zest.springbackend.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zest.springbackend.dto.ApiResponse;
import zest.springbackend.dto.NotificationRequest;
import zest.springbackend.service.NotificationService;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/notifications/session")
    public ApiResponse<String> sendNotification(@Valid @RequestBody NotificationRequest request) {
        String message = notificationService.sendSessionNotification(
            request.action(),
            request.sessionDate(),
            request.startTime(),
            request.endTime(),
            request.trainerName(),
            request.trainerEmail(),
            request.trainerPhone(),
            request.trainerTimezone(),
            request.leadTeacherName(),
            request.leadTeacherEmail(),
            request.leadTeacherTimezone(),
            request.memberSchoolName(),
            request.partnerSchoolName()
        );
        return ApiResponse.ok("Notification processed", message);
    }
}
