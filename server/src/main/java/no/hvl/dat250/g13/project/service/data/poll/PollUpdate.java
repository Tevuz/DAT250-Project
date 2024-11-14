package no.hvl.dat250.g13.project.service.data.poll;

import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Poll;
import no.hvl.dat250.g13.project.service.data.Validate;
import no.hvl.dat250.g13.project.service.data.option.OptionCreate;
import no.hvl.dat250.g13.project.service.data.option.OptionId;
import no.hvl.dat250.g13.project.service.data.option.OptionUpdate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static no.hvl.dat250.g13.project.service.data.Constraints.MESSAGE_POLL_ID_REQUIRED;

public record PollUpdate(
        @NotNull(message = MESSAGE_POLL_ID_REQUIRED)
        Long id,
        Optional<String> title,
        Optional<Integer> index,
        List<OptionCreate> option_additions,
        List<OptionUpdate> option_modifications,
        List<OptionId> option_deletions
) implements Validate {
    public Poll apply(Poll poll) {
        if (!Objects.equals(id, poll.getId()))
            throw new IllegalArgumentException("value mismatch");
        title.filter(Predicate.not(String::isBlank)).ifPresent(poll::setText);
        index.ifPresent(poll::setIndex);
        if (!option_deletions.isEmpty())
            poll.setOptions(poll.getOptions().stream()
                    .filter(option -> !option_deletions.contains(new OptionId(option.getId())))
                    .toList());
        if (!option_modifications.isEmpty())
            poll.getOptions().forEach(option -> option_modifications.stream()
                    .filter(mod -> mod.id().equals(option.getId()))
                    .forEach(mod -> mod.apply(option)));
        if (!option_additions.isEmpty())
            poll.setOptions(Stream.concat(poll.getOptions().stream(),
                    option_additions.stream().map(OptionCreate::into)).toList());
        return poll;
    }
}
