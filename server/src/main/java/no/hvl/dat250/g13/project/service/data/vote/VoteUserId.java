package no.hvl.dat250.g13.project.service.data.vote;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.service.data.Validate;

import static no.hvl.dat250.g13.project.service.data.Constraints.MESSAGE_VOTE_USER_ID_REQUIRED;

/**
 * @param user_id
 */
public record VoteUserId(
        @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
        Long user_id
) implements Validate {
}
