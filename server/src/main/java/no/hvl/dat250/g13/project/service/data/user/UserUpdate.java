package no.hvl.dat250.g13.project.service.data.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.service.data.validation.Validate;

import java.util.Objects;
import java.util.Optional;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.*;

/**
 * @param id
 * @param username
 */
public record UserUpdate(
        @NotNull(message = MESSAGE_USER_ID_REQUIRED)
        Long id,
        Optional<
                @Size(min = USERNAME_LENGTH_MIN, max = USERNAME_LENGTH_MAX, message = MESSAGE_USERNAME_LENGTH)
                @Pattern(regexp = USERNAME_PATTERN, message = MESSAGE_USERNAME_PATTERN)
                        String> username
) implements Validate {
    public UserUpdate(UserEntity entity) {
        this(entity.getId(), Optional.ofNullable(entity.getUsername()));
    }

    public UserEntity apply(UserEntity entity) {
        if (!Objects.equals(id, entity.getId()))
            throw new IllegalArgumentException("value mismatch");
        username.ifPresent(entity::setUsername);
        return entity;
    }
}
