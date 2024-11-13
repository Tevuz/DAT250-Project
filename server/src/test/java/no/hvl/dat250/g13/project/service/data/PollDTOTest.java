package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.hvl.dat250.g13.project.domain.Identifiers.OptionKey;
import no.hvl.dat250.g13.project.domain.Identifiers.PollKey;
import no.hvl.dat250.g13.project.domain.Option;
import no.hvl.dat250.g13.project.domain.Poll;
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
        var info = new PollDTO.Create("Poll", Optional.empty(), default_optionCreate());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void create_textMissing() {
        var info = new PollDTO.Create(null, Optional.empty(), default_optionCreate());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll text is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_textEmpty() {
        var info = new PollDTO.Create("", Optional.empty(), default_optionCreate());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll text is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_textBlank() {
        var info = new PollDTO.Create(" \n", Optional.empty(), default_optionCreate());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll text is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_optionsMissing() {
        var info = new PollDTO.Create("Poll", Optional.empty(), null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll options is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_optionsEmpty() {
        var info = new PollDTO.Create("Poll", Optional.empty(), List.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll options is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_into() {
        var poll = new PollDTO.Create("Poll", Optional.empty(), default_optionCreate()).into();
        assertEquals("Poll", poll.getText());
        assertEquals("Option", poll.getOptions().getFirst().getText());
        assertEquals(0, poll.getIndex());
    }

    @Test
    void update_ok() {
        var info = new PollDTO.Update(new PollKey(1L), Optional.empty(), Optional.empty(), List.of(), List.of(), List.of());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void update_idMissing() {
        var info = new PollDTO.Update(null, Optional.empty(), Optional.empty(), List.of(), List.of(), List.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Poll id is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_applyIdMismatch() {
        var poll = default_poll();

        var info = new PollDTO.Update(new PollKey(2L), Optional.empty(), Optional.empty(), List.of(), List.of(), List.of());
        assertThrows(IllegalArgumentException.class, () -> info.apply(poll), "Id mismatch");
    }

    @Test
    void update_applyNoChange() {
        var poll = default_poll();

        var info = new PollDTO.Update(new PollKey(1L), Optional.empty(), Optional.empty(), List.of(), List.of(), List.of());
        info.apply(poll);
        assertEquals(default_poll(), poll);
    }

    @Test
    void update_applyTitleChange() {
        var poll = default_poll();

        var info = new PollDTO.Update(new PollKey(1L), Optional.of("Changed"), Optional.empty(), List.of(), List.of(), List.of());
        info.apply(poll);
        assertEquals("Changed", poll.getText());
    }

    @Test
    void update_applyTitleEmpty() {
        var poll = default_poll();

        var info = new PollDTO.Update(new PollKey(1L), Optional.of(""), Optional.empty(), List.of(), List.of(), List.of());
        info.apply(poll);
        assertEquals("Poll", poll.getText());
    }

    @Test
    void update_applyTitleBlank() {
        var poll = default_poll();

        var info = new PollDTO.Update(new PollKey(1L), Optional.of(" \n"), Optional.empty(), List.of(), List.of(), List.of());
        info.apply(poll);
        assertEquals("Poll", poll.getText());
    }

    @Test
    void update_applyIndexChange() {
        var poll = default_poll();

        var info = new PollDTO.Update(new PollKey(1L), Optional.empty(), Optional.of(1),
                List.of(), List.of(), List.of());
        info.apply(poll);
        assertEquals(1, poll.getIndex());
    }

    @Test
    void update_applyOptionAdditions() {
        final var optionAdd = new OptionDTO.Create("Added", Optional.empty());

        var poll = default_poll();

        var info = new PollDTO.Update(new PollKey(1L), Optional.empty(), Optional.empty(),
                List.of(optionAdd), List.of(), List.of());
        info.apply(poll);
        assertEquals(3, poll.getOptions().size());
        assertEquals("Added", poll.getOptions().getLast().getText());
    }

    @Test
    void update_applyOptionModifications() {
        final var optionUpdate = new OptionDTO.Update(new OptionKey(2L), Optional.of("Changed"), Optional.empty());
        final var optionIgnore = new OptionDTO.Update(new OptionKey(9L), Optional.of("Ignored"), Optional.empty());

        var poll = default_poll();

        var info = new PollDTO.Update(new PollKey(1L), Optional.empty(), Optional.empty(),
                List.of(), List.of(optionUpdate, optionIgnore), List.of());
        info.apply(poll);
        assertEquals(2, poll.getOptions().size());
        assertEquals("Changed", poll.getOptions().get(1).getText());
        assertTrue(poll.getOptions().stream().noneMatch(option -> 9L == option.getId().id()));
    }

    @Test
    void update_applyOptionDeletions() {
        final var optionDelete = new OptionDTO.Id(new OptionKey(1L));
        final var optionIgnore = new OptionDTO.Id(new OptionKey(9L));

        var poll = default_poll();

        var info = new PollDTO.Update(new PollKey(1L), Optional.empty(), Optional.empty(),
                List.of(), List.of(), List.of(optionDelete, optionIgnore));
        info.apply(poll);
        assertEquals(1, poll.getOptions().size());
        assertTrue(poll.getOptions().stream().noneMatch(option -> 1L == option.getId().id()));
        assertTrue(poll.getOptions().stream() .anyMatch(option -> 2L == option.getId().id()));
        assertTrue(poll.getOptions().stream().noneMatch(option -> 9L == option.getId().id()));
    }

    @Test
    void id_ok() {
        var info = new PollDTO.Id(new PollKey(1L));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void id_idMissing() {
        {
            var info = new PollDTO.Id(null);
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Poll id is required", violations.iterator().next().getMessage());
        }
        {
            var info = new PollDTO.Id(new PollKey(null));
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("must not be null", violations.iterator().next().getMessage());
        }
    }

    Poll default_poll() {
        final var option1 = new Option();
        option1.setId(new OptionKey(1L));
        option1.setText("Option1");
        option1.setIndex(0);

        final var option2 = new Option();
        option2.setId(new OptionKey(2L));
        option2.setText("Option2");
        option2.setIndex(0);

       final var poll = new Poll();
       poll.setId(new PollKey(1L));
       poll.setText("Poll");
       poll.setIndex(0);
       poll.setOptions(List.of(option1, option2));
       return poll;
    }

    List<OptionDTO.Create> default_optionCreate() {
        return List.of(new OptionDTO.Create("Option", Optional.empty()));
    }
  
}