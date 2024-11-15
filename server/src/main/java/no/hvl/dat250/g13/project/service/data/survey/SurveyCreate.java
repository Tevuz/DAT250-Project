package no.hvl.dat250.g13.project.service.data.survey;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Survey;
import no.hvl.dat250.g13.project.service.data.validation.Validate;
import no.hvl.dat250.g13.project.service.data.poll.PollCreate;

import java.util.List;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.*;

public record SurveyCreate(
        @NotBlank(message = MESSAGE_SURVEY_TITLE_REQUIRED)
        String title,
        @NotNull(message = MESSAGE_SURVEY_AUTHOR_ID_REQUIRED)
        @Valid
        Long author,
        @NotEmpty(message = MESSAGE_SURVEY_POLLS_REQUIRED)
        List<PollCreate> polls
) implements Validate {
    public Survey into() {
        Survey survey = new Survey();
        survey.setTitle(title);
        survey.setAuthorId(author);
        survey.setPolls(polls.stream().map(PollCreate::into).toList());
        return survey;
    }
}
