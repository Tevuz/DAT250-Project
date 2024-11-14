package no.hvl.dat250.g13.project.service.data.user;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.service.data.Validate;

import static no.hvl.dat250.g13.project.service.data.Constraints.MESSAGE_USER_ID_REQUIRED;

/**
 *
 * @param id
 */
public record UserId(
        @NotNull(message = MESSAGE_USER_ID_REQUIRED)
        Long id
) implements Validate {
    public UserId(UserEntity entity) {
        this(entity.getId());
    }
}
