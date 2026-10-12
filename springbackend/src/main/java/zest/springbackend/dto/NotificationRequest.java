package zest.springbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequest(
    @NotBlank String action,
    @NotBlank String sessionDate,
    @NotBlank String startTime,
    @NotBlank String endTime,
    @NotBlank String trainerName,
    @NotBlank String trainerEmail,
    String trainerPhone,
    @NotBlank String trainerTimezone,
    @NotBlank String leadTeacherName,
    @NotBlank String leadTeacherEmail,
    @NotBlank String leadTeacherTimezone,
    @NotBlank String memberSchoolName,
    @NotBlank String partnerSchoolName
) {
}
