package no.hvl.dat250.g13.project.service.data.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.service.data.validation.Validate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.*;

/**
 *
 * @param id
 * @param username
 */
public record UserInfo(
        @NotNull(message = MESSAGE_USER_ID_REQUIRED)
        Long id,
        @NotNull(message = MESSAGE_USERNAME_REQUIRED)
        @Size(min = USERNAME_LENGTH_MIN, max = USERNAME_LENGTH_MAX, message = MESSAGE_USERNAME_LENGTH)
        @Pattern(regexp = USERNAME_PATTERN, message = MESSAGE_USERNAME_PATTERN)
        String username
) implements Validate {
    public UserInfo(UserEntity entity) {
        this(entity.getId(), entity.getUsername());
    }
}
