package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.OptionRef;
import no.hvl.dat250.g13.project.domain.Vote;

import java.util.List;
import java.util.Optional;

public record VoteInfo(
        Optional<Long> user_id,
        Optional<Long> survey_id,
        List<Long> options
) {
    public VoteInfo(Vote vote) {
        this(
            Optional.of(vote.getVoterId()),
            Optional.of(vote.getSurveyId()),
            vote.getOptions().stream().map(OptionRef::getOptionId).toList()
        );
    }

    public Vote into() {
        Vote vote = new Vote();
        id().ifPresent(vote::setId);
        survey_id.ifPresent((id) -> vote.setOptions(options.stream().map(option -> new OptionRef(id, option)).toList()));
        return vote;
    }

    public Optional<Vote.VoteKey> id() {
        if (user_id.isEmpty() || survey_id.isEmpty())
            return Optional.empty();
        return Optional.of(new Vote.VoteKey(user_id.get(), survey_id.get()));
    }
}
