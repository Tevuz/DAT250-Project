package no.hvl.dat250.g13.project.service.data.poll;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import no.hvl.dat250.g13.project.domain.Poll;
import no.hvl.dat250.g13.project.service.data.validation.Validate;
import no.hvl.dat250.g13.project.service.data.option.OptionCreate;

import java.util.List;
import java.util.Optional;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_POLL_OPTIONS_REQUIRED;
import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_POLL_TEXT_REQUIRED;

public record PollCreate(
        @NotBlank(message = MESSAGE_POLL_TEXT_REQUIRED)
        String title,
        Optional<Integer> index,
        @NotEmpty(message = MESSAGE_POLL_OPTIONS_REQUIRED)
        List<OptionCreate> options
) implements Validate {
    public Poll into() {
        Poll poll = new Poll();
        poll.setText(title);
        poll.setIndex(index.orElse(0));
        poll.setOptions(options.stream().map(OptionCreate::into).toList());
        return poll;
    }
}
