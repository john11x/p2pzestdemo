package zest.springbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import zest.springbackend.model.enums.UserRole;

public record CreateUserRequest(
    @NotBlank String fullName,
    @NotBlank @Email String email,
    String phoneNumber,
    @NotNull UserRole role,
    String schoolId,
    @NotBlank String timezone
) {
}
