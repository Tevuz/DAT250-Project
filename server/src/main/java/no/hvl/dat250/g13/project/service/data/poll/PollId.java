package no.hvl.dat250.g13.project.service.data.poll;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.service.data.validation.Validate;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_POLL_ID_REQUIRED;

public record PollId(
        @NotNull(message = MESSAGE_POLL_ID_REQUIRED)
        @Valid
        Long id
) implements Validate {
}
