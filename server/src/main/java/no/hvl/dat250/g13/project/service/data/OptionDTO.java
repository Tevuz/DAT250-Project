package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import no.hvl.dat250.g13.project.domain.Identifiers.OptionKey;
import no.hvl.dat250.g13.project.domain.Option;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class OptionDTO {

    // ---- Option Messages ----
    public static final String MESSAGE_OPTION_ID_REQUIRED = "Option id is required";
    public static final String MESSAGE_OPTION_TEXT_REQUIRED = "Option text is required";

    public record Create(
            @NotBlank(message = MESSAGE_OPTION_TEXT_REQUIRED)
            String text,
            Optional<Integer> index
    ) {
        public Option into() {
            Option option = new Option();
            option.setText(text);
            option.setIndex(index.orElse(0));
            return option;
        }
    }

    public record Update(
            @NotNull(message = MESSAGE_OPTION_ID_REQUIRED)
            OptionKey id,
            Optional<String> text,
            Optional<Integer> index
    ) {
        public Option apply(Option option) {
            if (!Objects.equals(id, option.getId()))
                throw new IllegalArgumentException("Id mismatch");
            text.filter(Predicate.not(String::isBlank)).ifPresent(option::setText);
            index.ifPresent(option::setIndex);
            return option;
        }
    }

    public record Info(
        OptionKey id,
        String text,
        Integer index,
        Optional<Integer> vote_count
    ) {
        public Info(Option option) {
            this(option.getId(), option.getText(), option.getIndex(), Optional.empty());
        }

        public Info(Option option, int vote_count) {
            this(option.getId(), option.getText(), option.getIndex(), Optional.of(vote_count));
        }
    }

    public record Id(
            @NotNull(message = MESSAGE_OPTION_ID_REQUIRED)
            OptionKey id
    ) { }

}
