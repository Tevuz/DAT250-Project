package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.Poll;

import java.util.List;
import java.util.Optional;

public record PollInfo(
        Optional<Long> id,
        Optional<String> text,
        Optional<Integer> order,
        List<OptionInfo> options
) {
    public PollInfo(Poll poll) {
        this(Optional.of(poll.getId()), Optional.of(poll.getText()), Optional.of(poll.getIndex()), poll.getOptions().stream().map(OptionInfo::new).toList());
    }

    public Poll into() {
        Poll poll = new Poll();
        poll.setText(text().orElse(""));
        poll.setIndex(order.orElse(0));
        poll.setOptions(options.stream().map(OptionInfo::into).toList());
        return poll;
    }
}
