package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.hvl.dat250.g13.project.domain.Option;
import no.hvl.dat250.g13.project.domain.Poll;
import no.hvl.dat250.g13.project.service.data.option.OptionCreate;
import no.hvl.dat250.g13.project.service.data.option.OptionId;
import no.hvl.dat250.g13.project.service.data.option.OptionUpdate;
import no.hvl.dat250.g13.project.service.data.poll.PollCreate;
import no.hvl.dat250.g13.project.service.data.poll.PollId;
import no.hvl.dat250.g13.project.service.data.poll.PollUpdate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
class PollDTOTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void create_ok() {
        var info = new PollCreate("Poll", Optional.empty(), default_optionCreate());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void create_textMissing() {
        var info = new PollCreate(null, Optional.empty(), default_optionCreate());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll text is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_textEmpty() {
        var info = new PollCreate("", Optional.empty(), default_optionCreate());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll text is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_textBlank() {
        var info = new PollCreate(" \n", Optional.empty(), default_optionCreate());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll text is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_optionsMissing() {
        var info = new PollCreate("Poll", Optional.empty(), null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll options is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_optionsEmpty() {
        var info = new PollCreate("Poll", Optional.empty(), List.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll options is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_into() {
        var poll = new PollCreate("Poll", Optional.empty(), default_optionCreate()).into();
        assertEquals("Poll", poll.getText());
        assertEquals("Option", poll.getOptions().getFirst().getText());
        assertEquals(0, poll.getIndex());
    }

    @Test
    void update_ok() {
        var info = new PollUpdate(1l, Optional.empty(), Optional.empty(), List.of(), List.of(), List.of());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void update_idMissing() {
        var info = new PollUpdate(null, Optional.empty(), Optional.empty(), List.of(), List.of(), List.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll value is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_applyIdMismatch() {
        var poll = default_poll();

        var info = new PollUpdate(2l, Optional.empty(), Optional.empty(), List.of(), List.of(), List.of());
        assertThrows(IllegalArgumentException.class, () -> info.apply(poll), "Id mismatch");
    }

    @Test
    void update_applyNoChange() {
        var poll = default_poll();

        var info = new PollUpdate(1l, Optional.empty(), Optional.empty(), List.of(), List.of(), List.of());
        info.apply(poll);
        assertEquals(default_poll(), poll);
    }

    @Test
    void update_applyTitleChange() {
        var poll = default_poll();

        var info = new PollUpdate(1l, Optional.of("Changed"), Optional.empty(), List.of(), List.of(), List.of());
        info.apply(poll);
        assertEquals("Changed", poll.getText());
    }

    @Test
    void update_applyTitleEmpty() {
        var poll = default_poll();

        var info = new PollUpdate(1l, Optional.of(""), Optional.empty(), List.of(), List.of(), List.of());
        info.apply(poll);
        assertEquals("Poll", poll.getText());
    }

    @Test
    void update_applyTitleBlank() {
        var poll = default_poll();

        var info = new PollUpdate(1l, Optional.of(" \n"), Optional.empty(), List.of(), List.of(), List.of());
        info.apply(poll);
        assertEquals("Poll", poll.getText());
    }

    @Test
    void update_applyIndexChange() {
        var poll = default_poll();

        var info = new PollUpdate(1l, Optional.empty(), Optional.of(1),
                List.of(), List.of(), List.of());
        info.apply(poll);
        assertEquals(1, poll.getIndex());
    }

    @Test
    void update_applyOptionAdditions() {
        final var optionAdd = new OptionCreate("Added", Optional.empty());

        var poll = default_poll();

        var info = new PollUpdate(1l, Optional.empty(), Optional.empty(),
                List.of(optionAdd), List.of(), List.of());
        info.apply(poll);
        assertEquals(3, poll.getOptions().size());
        assertEquals("Added", poll.getOptions().getLast().getText());
    }

    @Test
    void update_applyOptionModifications() {
        final var optionUpdate = new OptionUpdate(2l, Optional.of("Changed"), Optional.empty());
        final var optionIgnore = new OptionUpdate(9l, Optional.of("Ignored"), Optional.empty());

        var poll = default_poll();

        var info = new PollUpdate(1l, Optional.empty(), Optional.empty(),
                List.of(), List.of(optionUpdate, optionIgnore), List.of());
        info.apply(poll);
        assertEquals(2, poll.getOptions().size());
        assertEquals("Changed", poll.getOptions().get(1).getText());
        assertTrue(poll.getOptions().stream().noneMatch(option -> 9L == option.getId()));
    }

    @Test
    void update_applyOptionDeletions() {
        final var optionDelete = new OptionId(1l);
        final var optionIgnore = new OptionId(9l);

        var poll = default_poll();

        var info = new PollUpdate(1l, Optional.empty(), Optional.empty(),
                List.of(), List.of(), List.of(optionDelete, optionIgnore));
        info.apply(poll);
        assertEquals(1, poll.getOptions().size());
        assertTrue(poll.getOptions().stream().noneMatch(option -> 1L == option.getId()));
        assertTrue(poll.getOptions().stream() .anyMatch(option -> 2L == option.getId()));
        assertTrue(poll.getOptions().stream().noneMatch(option -> 9L == option.getId()));
    }

    @Test
    void id_ok() {
        var info = new PollId(1l);
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void id_idMissing() {
        {
            var info = new PollId(null);
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Poll value is required", violations.iterator().next().getMessage());
        }
    }

    Poll default_poll() {
        final var option1 = new Option();
        option1.setId(1l);
        option1.setText("Option1");
        option1.setIndex(0);

        final var option2 = new Option();
        option2.setId(2l);
        option2.setText("Option2");
        option2.setIndex(0);

       final var poll = new Poll();
       poll.setId(1l);
       poll.setText("Poll");
       poll.setIndex(0);
       poll.setOptions(List.of(option1, option2));
       return poll;
    }

    List<OptionCreate> default_optionCreate() {
        return List.of(new OptionCreate("Option", Optional.empty()));
    }
  
}