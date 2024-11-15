package no.hvl.dat250.g13.project.service.data.option;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Option;
import no.hvl.dat250.g13.project.service.data.validation.Validate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static no.hvl.dat250.g13.project.service.data.validation.Constraints.MESSAGE_OPTION_ID_REQUIRED;

public record OptionUpdate(
        @NotNull(message = MESSAGE_OPTION_ID_REQUIRED)
        Long id,
        Optional<String> text,
        Optional<Integer> index
) implements Validate {
    public Option apply(Option option) {
        if (!Objects.equals(id, option.getId()))
            throw new IllegalArgumentException("Id mismatch");
        text.filter(Predicate.not(String::isBlank)).ifPresent(option::setText);
        index.ifPresent(option::setIndex);
        return option;
    }
}
