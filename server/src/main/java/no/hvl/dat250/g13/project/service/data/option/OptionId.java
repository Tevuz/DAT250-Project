package no.hvl.dat250.g13.project.service.data.option;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.service.data.Validate;

import static no.hvl.dat250.g13.project.service.data.Constraints.MESSAGE_OPTION_ID_REQUIRED;

public record OptionId(
        @NotNull(message = MESSAGE_OPTION_ID_REQUIRED)
        Long id
) implements Validate {
}
