package zest.springbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record FindAvailableTrainersRequest(
    String schoolId,
    @NotBlank String date
) {
}
