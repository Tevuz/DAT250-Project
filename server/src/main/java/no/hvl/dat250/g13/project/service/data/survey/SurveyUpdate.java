package no.hvl.dat250.g13.project.service.data.survey;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Survey;
import no.hvl.dat250.g13.project.service.data.validation.Validate;
import no.hvl.dat250.g13.project.service.data.poll.PollCreate;
import no.hvl.dat250.g13.project.service.data.poll.PollId;
import no.hvl.dat250.g13.project.service.data.poll.PollUpdate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_SURVEY_ID_REQUIRED;

public record SurveyUpdate(
        @NotNull(message = MESSAGE_SURVEY_ID_REQUIRED)
        @Valid
        Long id,
        Optional<String> title,
        List<PollCreate> poll_additions,
        List<PollUpdate> poll_modifications,
        List<PollId> poll_deletions
) implements Validate {
    public Survey apply(Survey survey) {
        if (!Objects.equals(id, survey.getId()))
            throw new IllegalArgumentException("value mismatch");
        title.filter(Predicate.not(String::isBlank)).ifPresent(survey::setTitle);
        if (!poll_deletions.isEmpty())
            survey.setPolls(survey.getPolls().stream()
                    .filter(poll -> !poll_deletions.contains(new PollId(poll.getId())))
                    .toList());
        if (!poll_modifications.isEmpty())
            survey.getPolls().forEach(poll -> poll_modifications.stream()
                    .filter(mod -> mod.id().equals(poll.getId()))
                    .forEach(mod -> mod.apply(poll)));
        if (!poll_additions.isEmpty())
            survey.setPolls(Stream.concat(survey.getPolls().stream(),
                    poll_additions.stream().map(PollCreate::into)).toList());
        return survey;
    }
}
