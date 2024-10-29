package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.Identifiers.OptionKey;
import no.hvl.dat250.g13.project.domain.Option;

import java.util.Optional;

public record OptionInfo(
        Optional<OptionKey> id,
        Optional<Integer> order,
        Optional<String> text,
        Optional<Integer> voteCount
) {
    public OptionInfo(Option option, Integer voteCount) {
        this(Optional.ofNullable(option.getId()), Optional.of(option.getIndex()), Optional.ofNullable(option.getText()), Optional.ofNullable(voteCount));
    }

    public OptionInfo(Option option) {
        this(option, null);
    }

    public Option into() {
        Option option = new Option();
        option.setId(id.orElse(null));
        option.setIndex(order.orElse(0));
        option.setText(text().orElse(""));
        return option;
    }
}
