package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.Survey;

import java.util.List;

public record SurveyInfo(
        Long id,
        String title,
        List<PollInfo> polls,
        Integer voteTotal
) {
    public SurveyInfo(Survey survey, Integer voteTotal) {
        this(survey.getId(), survey.getTitle(), survey.getPolls().stream().map(PollInfo::new).toList(), voteTotal);
    }
}
