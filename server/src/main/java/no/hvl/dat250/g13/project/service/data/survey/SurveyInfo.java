package no.hvl.dat250.g13.project.service.data.survey;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Survey;
import no.hvl.dat250.g13.project.service.data.validation.Validate;
import no.hvl.dat250.g13.project.service.data.poll.PollInfo;

import java.util.List;
import java.util.Optional;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_SURVEY_ID_REQUIRED;
import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_SURVEY_TITLE_REQUIRED;

public record SurveyInfo(
        @NotNull(message = MESSAGE_SURVEY_ID_REQUIRED)
        Long id,
        @NotBlank(message = MESSAGE_SURVEY_TITLE_REQUIRED)
        String title,
        List<PollInfo> polls,
        Optional<Long> voteTotal
) implements Validate {
    public SurveyInfo(Survey survey) {
        this(survey.getId(), survey.getTitle(), survey.getPolls().stream().map(PollInfo::new).toList(), Optional.ofNullable(survey.getVoteTotal()));
    }
}
