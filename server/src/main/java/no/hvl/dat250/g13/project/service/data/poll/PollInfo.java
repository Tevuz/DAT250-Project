package no.hvl.dat250.g13.project.service.data.poll;

import no.hvl.dat250.g13.project.domain.Poll;
import no.hvl.dat250.g13.project.service.data.validation.Validate;
import no.hvl.dat250.g13.project.service.data.option.OptionInfo;

import java.util.List;

public record PollInfo(
        Long id,
        String title,
        int order,
        List<OptionInfo> options
) implements Validate {
    public PollInfo(Poll poll) {
        this(poll.getId(), poll.getText(), poll.getIndex(), poll.getOptions().stream().map(OptionInfo::new).toList());
    }
}
