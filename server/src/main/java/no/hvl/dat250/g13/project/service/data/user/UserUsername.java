package no.hvl.dat250.g13.project.service.data.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.service.data.Validate;

import static no.hvl.dat250.g13.project.service.data.Constraints.*;

/**
 * @param username
 */
public record UserUsername(
        @NotBlank(message = MESSAGE_USERNAME_REQUIRED)
        @Size(min = USERNAME_LENGTH_MIN, max = USERNAME_LENGTH_MAX, message = MESSAGE_USERNAME_LENGTH)
        @Pattern(regexp = USERNAME_PATTERN, message = MESSAGE_USERNAME_PATTERN)
        String username
) implements Validate {
    public UserUsername(UserEntity entity) {
        this(entity.getUsername());
    }
}
