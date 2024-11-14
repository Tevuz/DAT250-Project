package no.hvl.dat250.g13.project.service.data.option;

import jakarta.validation.constraints.NotBlank;
import no.hvl.dat250.g13.project.domain.Option;
import no.hvl.dat250.g13.project.service.data.Validate;

import java.util.Optional;

import static no.hvl.dat250.g13.project.service.data.Constraints.MESSAGE_OPTION_TEXT_REQUIRED;

public record OptionCreate(
        @NotBlank(message = MESSAGE_OPTION_TEXT_REQUIRED)
        String text,
        Optional<Integer> index
) implements Validate {
    public Option into() {
        Option option = new Option();
        option.setText(text);
        option.setIndex(index.orElse(0));
        return option;
    }
}
