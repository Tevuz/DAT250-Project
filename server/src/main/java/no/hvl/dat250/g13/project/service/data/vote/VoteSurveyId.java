package no.hvl.dat250.g13.project.service.data.vote;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.service.data.validation.Validate;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_VOTE_SURVEY_ID_REQUIRED;

/**
 * @param survey_id
 */
public record VoteSurveyId(
        @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
        Long survey_id
) implements Validate {
}
