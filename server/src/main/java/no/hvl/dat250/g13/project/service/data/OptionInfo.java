package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.Option;

import java.util.Optional;

public record OptionInfo(
        Optional<Long> id,
        Optional<Integer> order,
        Optional<String> text,
        Optional<Integer> voteCount
) {
    public OptionInfo(Option option, Integer voteCount) {
        this(Optional.of(option.getId()), Optional.of(option.getIndex()), Optional.of(option.getText()), Optional.of(voteCount));
    }

    public OptionInfo(Option option) {
        this(option, 0);
    }

    public Option into() {
        Option option = new Option();
        option.setIndex(order.orElse(0));
        option.setText(text().orElse(""));
        return null;
    }
}
