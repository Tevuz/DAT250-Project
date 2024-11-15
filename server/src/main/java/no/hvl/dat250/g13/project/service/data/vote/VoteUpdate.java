package no.hvl.dat250.g13.project.service.data.vote;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.service.data.validation.Validate;
import org.springframework.data.util.Streamable;

import java.util.Set;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_VOTE_SURVEY_ID_REQUIRED;
import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_VOTE_USER_ID_REQUIRED;

/**
 * @param user_id
 * @param survey_id
 * @param mode
 * @param options
 */
public record VoteUpdate(
        @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
        Long user_id,
        @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
        Long survey_id,
        @Pattern(regexp = "^(REPLACE|UNION|INTERSECT)$")
        String mode,
        Set<Long> options
) implements Validate {
    public VoteUpdate(Vote vote) {
        this(vote.getUserId(), vote.getSurveyId(), "REPLACE", Set.of(vote.getOptionId()));
    }

    public void apply(Set<Vote> votes, Set<Vote> save, Set<Vote> delete) {
        var modification = Streamable.of(options).map(option -> new Vote(user_id, survey_id, option)).toSet();
        switch (mode) {
            case "REPLACE":
                save.addAll(modification);
                delete.addAll(votes);
                delete.removeAll(save);
                break;
            case "UNION":
                save.addAll(votes);
                save.addAll(modification);
                break;
            case "INTERSECT":
                save.addAll(votes);
                save.retainAll(modification);
                delete.addAll(votes);
                delete.removeAll(modification);
                break;
        }
    }
}
