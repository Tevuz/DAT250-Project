package no.hvl.dat250.g13.project.service.data.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.service.data.validation.Validate;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.*;

/**
 * @param username
 */
public record UserCreate(
        @NotNull(message = MESSAGE_USERNAME_REQUIRED)
        @Size(min = USERNAME_LENGTH_MIN, max = USERNAME_LENGTH_MAX, message = MESSAGE_USERNAME_LENGTH)
        @Pattern(regexp = USERNAME_PATTERN, message = MESSAGE_USERNAME_PATTERN)
        String username
) implements Validate {
    public UserCreate(UserEntity entity) {
        this(entity.getUsername());
    }

    public UserEntity into() {
        var entity = new UserEntity();
        entity.setUsername(username);
        return entity;
    }
}
