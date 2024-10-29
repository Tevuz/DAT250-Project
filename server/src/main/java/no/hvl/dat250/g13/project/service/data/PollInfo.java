package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.Identifiers.PollKey;
import no.hvl.dat250.g13.project.domain.Poll;

import java.util.List;
import java.util.Optional;

public record PollInfo(
        Optional<PollKey> id,
        Optional<String> text,
        Optional<Integer> index,
        List<OptionInfo> options
) {
    public PollInfo(Poll poll) {
        this(Optional.ofNullable(poll.getId()),
                Optional.ofNullable(poll.getText()),
                Optional.of(poll.getIndex()),
                poll.getOptions().stream().map(OptionInfo::new).toList());
    }

    public Poll into() {
        Poll poll = new Poll();
        poll.setId(id.orElse(null));
        poll.setText(text().orElse(""));
        poll.setIndex(index.orElse(0));
        poll.setOptions(options.stream().map(OptionInfo::into).toList());
        return poll;
    }
}
