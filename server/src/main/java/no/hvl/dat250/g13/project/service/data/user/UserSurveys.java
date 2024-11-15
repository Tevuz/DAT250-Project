package no.hvl.dat250.g13.project.service.data.user;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Survey;
import no.hvl.dat250.g13.project.service.data.validation.Validate;
import org.springframework.data.util.Streamable;

import java.util.Set;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_USER_ID_REQUIRED;
import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_VOTE_OPTIONS_REQUIRED;

/**
 * @param user_id
 * @param surveys
 */
public record UserSurveys(
        @NotNull(message = MESSAGE_USER_ID_REQUIRED)
        Long user_id,
        @NotNull(message = MESSAGE_VOTE_OPTIONS_REQUIRED)
        Set<Long> surveys
) implements Validate {
    public UserSurveys(Long user_id, Iterable<Survey> votes) {
        this(user_id, Streamable.of(votes).map(Survey::getId).toSet());
    }
}
