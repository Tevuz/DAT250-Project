package no.hvl.dat250.g13.project.service.data.vote;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.service.data.validation.Validate;
import org.springframework.data.util.Streamable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_VOTE_OPTIONS_REQUIRED;
import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_VOTE_SURVEY_ID_REQUIRED;

/**
 * @param survey_id
 * @param options
 */
public record VoteSurveyVotes(
        @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
        Long survey_id,
        @NotNull(message = MESSAGE_VOTE_OPTIONS_REQUIRED)
        // Map<userId, Set<optionId>>
        Map<Long, Set<Long>> options
) implements Validate {
    public VoteSurveyVotes(Long survey_id, Iterable<Vote> votes) {
        this(survey_id, Streamable.of(votes).stream().collect(Collectors.groupingBy(Vote::getUserId, Collectors.mapping(Vote::getOptionId, Collectors.toSet()))));
    }
}
