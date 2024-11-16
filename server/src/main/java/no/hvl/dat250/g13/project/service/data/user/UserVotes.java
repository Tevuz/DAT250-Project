package no.hvl.dat250.g13.project.service.data.user;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.service.data.validation.Validate;
import org.springframework.data.util.Streamable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.*;

/**
 * @param user_id
 * @param votes
 */
public record UserVotes(
        @NotNull(message = MESSAGE_USER_ID_REQUIRED)
        Long user_id,
        @NotNull(message = MESSAGE_VOTE_OPTIONS_REQUIRED)
        // Map<surveyId, Set<optionId>>
        Map<Long, Set<Long>> votes
) implements Validate {
    public UserVotes(Long user_id, Iterable<Vote> votes) {
        this(user_id, Streamable.of(votes).stream().collect(Collectors.groupingBy(Vote::getSurveyId, Collectors.mapping(Vote::getOptionId, Collectors.toSet()))));
    }
}
