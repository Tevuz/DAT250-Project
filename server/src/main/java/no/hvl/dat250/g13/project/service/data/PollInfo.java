package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.Poll;

import java.util.List;

public record PollInfo(
        Long id,
        int order,
        List<OptionInfo> options
) {
    public PollInfo(Poll poll) {
        this(poll.getId(), poll.getOrder(), poll.getOptions().stream().map(OptionInfo::new).toList());
    }
}
