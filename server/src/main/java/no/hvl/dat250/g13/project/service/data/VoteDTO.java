package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.hvl.dat250.g13.project.domain.Identifiers.OptionKey;
import no.hvl.dat250.g13.project.domain.Identifiers.SurveyKey;
import no.hvl.dat250.g13.project.domain.Identifiers.UserKey;
import no.hvl.dat250.g13.project.domain.Vote;
import org.springframework.data.util.Streamable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class VoteDTO {

    // ---- Vote Messages ----
    public static final String MESSAGE_VOTE_SURVEY_ID_REQUIRED = "Survey value is required";
    public static final String MESSAGE_VOTE_USER_ID_REQUIRED = "User value is required";
    public static final String MESSAGE_VOTE_OPTIONS_REQUIRED = "Selected options is required";

    /**
     *
     * @param user_id
     * @param survey_id
     * @param options
     */
    public record Create(
            @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
            UserKey user_id,
            @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
            SurveyKey survey_id,
            @NotNull(message = MESSAGE_VOTE_OPTIONS_REQUIRED)
            Set<OptionKey> options
    ) {
        public Set<Vote> into() {
            return Streamable.of(options).map(option -> new Vote(user_id, survey_id, option)).toSet();
        }
    }

    /**
     *
     * @param user_id
     * @param survey_id
     * @param mode
     * @param options
     */
    public record Update(
            @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
            UserKey user_id,
            @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
            SurveyKey survey_id,
            @Pattern(regexp = "^(REPLACE|UNION|INTERSECT)$")
            String mode,
            Set<OptionKey> options
    ) {
        public Update(Vote vote) {
            this(vote.getUserId(), vote.getSurveyId(), "REPLACE", Set.of(vote.getOptionId()));
        }

        public void apply(Set<Vote> votes, Set<Vote> save, Set<Vote> delete) {
            var modification = Streamable.of(options).map(option -> new Vote(user_id, survey_id, option)).toSet();
            switch (mode) {
                case "REPLACE":
                    save.addAll(modification);
                    delete.addAll(votes);
                    delete.removeAll(save);
                    break;
                case "UNION":
                    save.addAll(votes);
                    save.addAll(modification);
                    break;
                case "INTERSECT":
                    save.addAll(votes);
                    save.retainAll(modification);
                    delete.addAll(votes);
                    delete.removeAll(modification);
                    break;
            }
        }
    }

    /**
     *
     * @param user_id
     * @param survey_id
     * @param options
     */
    public record Info(
            @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
            UserKey user_id,
            @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
            SurveyKey survey_id,
            @NotNull(message = MESSAGE_VOTE_OPTIONS_REQUIRED)
            Set<OptionKey> options
    ) {
        public Info(UserKey user_id, SurveyKey survey_id, Iterable<Vote> votes) {
            this(user_id, survey_id, Streamable.of(votes).map(Vote::getOptionId).toSet());
        }

        public static Iterable<VoteDTO.Info> from(Iterable<Vote> votes) {
            var map = Streamable.of(votes).stream().collect(Collectors.groupingBy(VoteDTO.Id::new, Collectors.mapping(Vote::getOptionId, Collectors.toSet())));
            return Streamable.of(map.entrySet()).map(entry -> new Info(entry.getKey().user_id(), entry.getKey().survey_id(), entry.getValue()));
        }
    }

    /**
     *
     * @param user_id
     * @param options
     */
    public record UserVotes(
            @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
            UserKey user_id,
            @NotNull(message = MESSAGE_VOTE_OPTIONS_REQUIRED)
            Map<SurveyKey, Set<OptionKey>> options
    ) {
        public UserVotes(UserKey user_id, Iterable<Vote> votes) {
            this(user_id, Streamable.of(votes).stream().collect(Collectors.groupingBy(Vote::getSurveyId, Collectors.mapping(Vote::getOptionId, Collectors.toSet()))));
        }
    }

    /**
     *
     * @param survey_id
     * @param options
     */
    public record SurveyVotes(
            @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
            SurveyKey survey_id,
            @NotNull(message = MESSAGE_VOTE_OPTIONS_REQUIRED)
            Map<UserKey, Set<OptionKey>> options
    ) {
        public SurveyVotes(SurveyKey survey_id, Iterable<Vote> votes) {
            this(survey_id, Streamable.of(votes).stream().collect(Collectors.groupingBy(Vote::getUserId, Collectors.mapping(Vote::getOptionId, Collectors.toSet()))));
        }
    }

    /**
     *
     * @param user_id
     * @param survey_id
     */
    public record Id(
            @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
            UserKey user_id,
            @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
            SurveyKey survey_id
    ) {
        public Id(Vote vote) {
            this(vote.getUserId(), vote.getSurveyId());
        }
    }

    /**
     *
     * @param user_id
     */
    public record UserId(
            @NotNull(message = MESSAGE_VOTE_USER_ID_REQUIRED)
            UserKey user_id
    ) { }

    /**
     *
     * @param survey_id
     */
    public record SurveyId(
            @NotNull(message = MESSAGE_VOTE_SURVEY_ID_REQUIRED)
            SurveyKey survey_id
    ) { }
}
