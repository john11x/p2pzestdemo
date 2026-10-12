package zest.springbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import zest.springbackend.model.enums.DayOfWeek;

public record CreateSchoolAvailabilityRequest(
    @NotBlank String schoolId,
    @NotNull DayOfWeek dayOfWeek,
    @NotBlank String startTime,
    @NotBlank String endTime,
    @NotBlank String timezone,
    @NotNull Boolean isRecurring,
    String startDate,
    String endDate
) {
}
