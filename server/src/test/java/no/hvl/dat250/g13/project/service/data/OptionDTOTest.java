package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.hvl.dat250.g13.project.domain.Option;
import no.hvl.dat250.g13.project.service.data.option.OptionCreate;
import no.hvl.dat250.g13.project.service.data.option.OptionId;
import no.hvl.dat250.g13.project.service.data.option.OptionUpdate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OptionDTOTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void create_ok() {
        var info = new OptionCreate("Option", Optional.empty());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void create_textBlank() {
        var info = new OptionCreate("", Optional.empty());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Option text is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_textMissing() {
        var info = new OptionCreate(null, Optional.empty());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Option text is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_into() {
        {
            var option = new OptionCreate("Option", Optional.empty()).into();
            assertEquals("Option", option.getText());
            assertEquals(0, option.getIndex());
        }
        {
            var option = new OptionCreate("Option", Optional.of(1)).into();
            assertEquals("Option", option.getText());
            assertEquals(1, option.getIndex());
        }
    }

    @Test
    void update_ok() {
        var info = new OptionUpdate(1l, Optional.empty(), Optional.empty());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void update_idMissing() {
        var info = new OptionUpdate(null, Optional.empty(), Optional.empty());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Option value is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_applyNoChange() {
        var option = new Option();
        option.setId(1l);
        option.setText("Option");
        option.setIndex(0);

        var info = new OptionUpdate(1l, Optional.empty(), Optional.empty());
        info.apply(option);
        assertEquals("Option", option.getText());
        assertEquals(0, option.getIndex());
    }

    @Test
    void update_applyIdMismatch() {
        var option = new Option();
        option.setId(1l);
        option.setIndex(0);
        option.setText("Option");

        var info = new OptionUpdate(2l, Optional.empty(), Optional.of(1));
        assertThrows(IllegalArgumentException.class, () -> info.apply(option), "Id mismatch");
    }

    @Test
    void update_applyTextChange() {
        var option = new Option();
        option.setId(1l);
        option.setIndex(0);
        option.setText("Option");

        var info = new OptionUpdate(1l, Optional.of("Changed"), Optional.empty());
        info.apply(option);
        assertEquals("Changed", option.getText());
        assertEquals(0, option.getIndex());
    }

    @Test
    void update_applyTextEmpty() {
        var option = new Option();
        option.setId(1l);
        option.setIndex(0);
        option.setText("Option");

        var info = new OptionUpdate(1l, Optional.of(""), Optional.empty());
        info.apply(option);
        assertEquals("Option", option.getText());
    }

    @Test
    void update_applyTextBlank() {
        var option = new Option();
        option.setId(1l);
        option.setIndex(0);
        option.setText("Option");

        var info = new OptionUpdate(1l, Optional.of(" \n"), Optional.empty());
        info.apply(option);
        assertEquals("Option", option.getText());
    }

    @Test
    void update_applyIndexChange() {
        var option = new Option();
        option.setId(1l);
        option.setIndex(0);
        option.setText("Option");

        var info = new OptionUpdate(1l, Optional.empty(), Optional.of(1));
        info.apply(option);
        assertEquals("Option", option.getText());
        assertEquals(1, option.getIndex());
    }

    @Test
    void id_ok() {
        var info = new OptionId(1l);
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void id_idMissing() {
        var info = new OptionId(null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Option value is required", violations.iterator().next().getMessage());
    }
}