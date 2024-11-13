package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Identifiers.PollKey;
import no.hvl.dat250.g13.project.domain.Poll;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class PollDTO {

    // ---- Poll Messages ----
    public static final String MESSAGE_POLL_ID_REQUIRED = "Poll value is required";
    public static final String MESSAGE_POLL_TEXT_REQUIRED = "Poll text is required";
    public static final String MESSAGE_POLL_OPTIONS_REQUIRED = "Poll options is required";

    public record Create(
            @NotBlank(message = MESSAGE_POLL_TEXT_REQUIRED)
            String title,
            Optional<Integer> index,
            @NotEmpty(message = MESSAGE_POLL_OPTIONS_REQUIRED)
            List<OptionDTO.Create> options
    ) implements Validate {
        public Poll into() {
            Poll poll = new Poll();
            poll.setText(title);
            poll.setIndex(index.orElse(0));
            poll.setOptions(options.stream().map(OptionDTO.Create::into).toList());
            return poll;
        }
    }

    public record Update(
            @NotNull(message = MESSAGE_POLL_ID_REQUIRED)
            PollKey id,
            Optional<String> title,
            Optional<Integer> index,
            List<OptionDTO.Create> option_additions,
            List<OptionDTO.Update> option_modifications,
            List<OptionDTO.Id> option_deletions
    ) implements Validate {
        public Poll apply(Poll poll) {
            if (!Objects.equals(id, poll.getId()))
                throw new IllegalArgumentException("value mismatch");
            title.filter(Predicate.not(String::isBlank)).ifPresent(poll::setText);
            index.ifPresent(poll::setIndex);
            if (!option_deletions.isEmpty())
                    poll.setOptions(poll.getOptions().stream()
                            .filter(option -> !option_deletions.contains(new OptionDTO.Id(option.getId())))
                            .toList());
            if (!option_modifications.isEmpty())
                    poll.getOptions().forEach(option -> option_modifications.stream()
                            .filter(mod -> mod.id().equals(option.getId()))
                            .forEach(mod -> mod.apply(option)));
            if (!option_additions.isEmpty())
                    poll.setOptions(Stream.concat(poll.getOptions().stream(),
                            option_additions.stream().map(OptionDTO.Create::into)).toList());
            return poll;
        }
    }

    public record Info(
            PollKey id,
            String title,
            int order,
            List<OptionDTO.Info> options
    ) implements Validate {
        public Info(Poll poll) {
            this(poll.getId(), poll.getText(), poll.getIndex(), poll.getOptions().stream().map(OptionDTO.Info::new).toList());
        }
    }

    public record Id(
            @NotNull(message = MESSAGE_POLL_ID_REQUIRED)
            @Valid
            PollKey id
    ) implements Validate { }
}
