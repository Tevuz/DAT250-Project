package no.hvl.dat250.g13.project.service.data.option;

import no.hvl.dat250.g13.project.domain.Option;
import no.hvl.dat250.g13.project.service.data.validation.Validate;

import java.util.Optional;

public record OptionInfo(
        Long id,
        String text,
        Integer index,
        Optional<Long> vote_count
) implements Validate {
    public OptionInfo(Option option) {
        this(option.getId(), option.getText(), option.getIndex(), Optional.empty());
    }
}
