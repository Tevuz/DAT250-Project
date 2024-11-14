package no.hvl.dat250.g13.project.service.data.vote;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.service.data.Validate;
import org.springframework.data.util.Streamable;

import java.util.Set;

import static no.hvl.dat250.g13.project.service.data.Constraints.*;

/**
 * @param user_id
 * @param survey_id
 * @param options
 */
public record VoteCreate(
        @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
        Long user_id,
        @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
        Long survey_id,
        @NotNull(message = MESSAGE_VOTE_OPTIONS_REQUIRED)
        Set<Long> options
) implements Validate {
    public Set<Vote> into() {
        return Streamable.of(options).map(option -> new Vote(user_id, survey_id, option)).toSet();
    }
}
