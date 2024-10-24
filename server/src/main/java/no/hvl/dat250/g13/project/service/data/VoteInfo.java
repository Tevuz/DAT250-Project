package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.OptionRef;
import no.hvl.dat250.g13.project.domain.Vote;

import java.util.List;

public record VoteInfo(
        Long id,
        Long user_id,
        Long survey_id,
        List<Long> options
) {
    public VoteInfo(Vote vote) {
        this(
            vote.getId(),
            vote.getVoterId(),
            vote.getSurveyId(),
            vote.getOptions().stream().map(OptionRef::getOptionId).toList()
        );
    }

    public Vote into() {
        return new Vote(
                id,
                user_id,
                survey_id,
                options.stream().map((optionId) -> new OptionRef(id, optionId)).toList()
        );
    }
}
