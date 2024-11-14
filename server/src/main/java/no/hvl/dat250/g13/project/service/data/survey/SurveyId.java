package no.hvl.dat250.g13.project.service.data.survey;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.service.data.Validate;

import static no.hvl.dat250.g13.project.service.data.Constraints.MESSAGE_SURVEY_ID_REQUIRED;

public record SurveyId(
        @NotNull(message = MESSAGE_SURVEY_ID_REQUIRED)
        Long id
) implements Validate {
}
