package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.Option;

public record OptionInfo(
        Long id,
        int order,
        String text,
        Integer voteCount
) {
    public OptionInfo(Option option, Integer voteCount) {
        this(option.getId(), option.getOrder(), option.getText(), voteCount);
    }

    public OptionInfo(Option option) {
        this(option.getId(), option.getOrder(), option.getText(), null);
    }
}
