package no.hvl.dat250.g13.project.service.data.vote;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.service.data.Validate;
import org.springframework.data.util.Streamable;

import java.util.Set;
import java.util.stream.Collectors;

import static no.hvl.dat250.g13.project.service.data.Constraints.*;

/**
 * @param user_id
 * @param survey_id
 * @param options
 */
public record VoteInfo(
        @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
        Long user_id,
        @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
        Long survey_id,
        @NotNull(message = MESSAGE_VOTE_OPTIONS_REQUIRED)
        Set<Long> options
) implements Validate {
    public VoteInfo(Long user_id, Long survey_id, Iterable<Vote> votes) {
        this(user_id, survey_id, votes == null ? null : Streamable.of(votes).map(Vote::getOptionId).toSet());
    }

    public static Iterable<VoteInfo> from(Iterable<Vote> votes) {
        var map = Streamable.of(votes).stream().collect(Collectors.groupingBy(VoteId::new, Collectors.mapping(Vote::getOptionId, Collectors.toSet())));
        return Streamable.of(map.entrySet()).map(entry -> new VoteInfo(entry.getKey().user_id(), entry.getKey().survey_id(), entry.getValue()));
    }
}
