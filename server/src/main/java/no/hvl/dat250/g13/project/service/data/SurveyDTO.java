package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Identifiers.SurveyKey;
import no.hvl.dat250.g13.project.domain.Identifiers.UserKey;
import no.hvl.dat250.g13.project.domain.Survey;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class SurveyDTO {

    // ---- SurveyDTO Messages ----
    public static final String MESSAGE_SURVEY_ID_REQUIRED = "Survey value is required";
    public static final String MESSAGE_SURVEY_AUTHOR_ID_REQUIRED = "Survey author value is required";
    public static final String MESSAGE_SURVEY_TITLE_REQUIRED = "Survey title is required";
    public static final String MESSAGE_SURVEY_POLLS_REQUIRED = "Survey polls is required";

    public record Create(
            @NotBlank(message = MESSAGE_SURVEY_TITLE_REQUIRED)
            String title,
            @NotNull(message = MESSAGE_SURVEY_AUTHOR_ID_REQUIRED)
            @Valid
            UserKey author,
            @NotEmpty(message = MESSAGE_SURVEY_POLLS_REQUIRED)
            List<PollDTO.Create> polls
    ) {
        public Survey into() {
            Survey survey = new Survey();
            survey.setTitle(title);
            survey.setAuthor(author);
            survey.setPolls(polls.stream().map(PollDTO.Create::into).toList());
            return survey;
        }
    }

    public record Update(
            @NotNull(message = MESSAGE_SURVEY_ID_REQUIRED)
            @Valid
            SurveyKey id,
            Optional<String> title,
            List<PollDTO.Create> poll_additions,
            List<PollDTO.Update> poll_modifications,
            List<PollDTO.Id> poll_deletions
    ) {
        public Survey apply(Survey survey) {
            if (!Objects.equals(id, survey.getId()))
                throw new IllegalArgumentException("value mismatch");
            title.filter(Predicate.not(String::isBlank)).ifPresent(survey::setTitle);
            if (!poll_deletions.isEmpty())
                    survey.setPolls(survey.getPolls().stream()
                            .filter(poll -> !poll_deletions.contains(new PollDTO.Id(poll.getId())))
                            .toList());
            if (!poll_modifications.isEmpty())
                    survey.getPolls().forEach(poll -> poll_modifications.stream()
                            .filter(mod -> mod.id().equals(poll.getId()))
                            .forEach(mod -> mod.apply(poll)));
            if (!poll_additions.isEmpty())
                    survey.setPolls(Stream.concat(survey.getPolls().stream(),
                        poll_additions.stream().map(PollDTO.Create::into)).toList());
            return survey;
        }
    }

    public record Info(
            @NotNull(message = MESSAGE_SURVEY_ID_REQUIRED)
            @Valid
            SurveyKey id,
            @NotBlank(message = MESSAGE_SURVEY_TITLE_REQUIRED)
            String title,
            List<PollDTO.Info> polls,
            Optional<Integer> voteTotal
    ) {
        public Info(Survey survey) {
            this(survey.getId(), survey.getTitle(), survey.getPolls().stream().map(PollDTO.Info::new).toList(), Optional.empty());
        }

        public Info(Survey survey, int votes) {
            this(survey.getId(), survey.getTitle(), survey.getPolls().stream().map(PollDTO.Info::new).toList(), Optional.of(votes));
        }
    }

    public record Id(
            @NotNull(message = MESSAGE_SURVEY_ID_REQUIRED)
            @Valid
            SurveyKey id
    ) { }
}
