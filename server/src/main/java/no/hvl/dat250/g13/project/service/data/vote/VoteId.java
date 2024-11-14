package no.hvl.dat250.g13.project.service.data.vote;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.service.data.Validate;

import static no.hvl.dat250.g13.project.service.data.Constraints.MESSAGE_VOTE_SURVEY_ID_REQUIRED;
import static no.hvl.dat250.g13.project.service.data.Constraints.MESSAGE_VOTE_USER_ID_REQUIRED;

/**
 * @param user_id
 * @param survey_id
 */
public record VoteId(
        @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
        Long user_id,
        @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
        Long survey_id
) implements Validate {
    public VoteId(Vote vote) {
        this(vote.getUserId(), vote.getSurveyId());
    }
}
