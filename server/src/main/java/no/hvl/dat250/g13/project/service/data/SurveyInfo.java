package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.Survey;

import java.util.List;
import java.util.Optional;

public record SurveyInfo(
        Optional<Long> id,
        Optional<String> title,
        Optional<List<PollInfo>> polls,
        Optional<Integer> voteTotal
) {
    public SurveyInfo(Survey survey, Integer voteTotal) {
        this(Optional.ofNullable(survey.getId()),
             Optional.ofNullable(survey.getTitle()),
             Optional.of(survey.getPolls().stream().map(PollInfo::new).toList()),
             Optional.ofNullable(voteTotal));
    }

    public SurveyInfo(Survey survey) {
        this(survey, null);
    }

    public Survey into() {
        Survey survey = new Survey();
        survey.setId(id.orElse(null));
        survey.setTitle(title().orElse(""));
        survey.setPolls(polls().orElse(List.of()).stream().map(PollInfo::into).toList());
        return survey;
    }
}
