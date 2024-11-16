package no.hvl.dat250.g13.project.service.data.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MutualExclusiveValidatorTest {

    @MutualExclusive(fields = {"object", "optional", "collection", "map", "iterator", "array"})
    record Data(
        Object object,
        Optional<Object> optional,
        Collection<Object> collection,
        Map<Object, Object> map,
        Iterator<Object> iterator,
        Object[] array
    ) {}

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void object_ok() {
        var data = new Data(1, null,null, null, null, null);
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

    @Test
    void object_null() {
        var data = new Data(null, null ,null, null, null, null);
        var violations = validator.validate(data);
        assertEquals(1, violations.size());
        assertEquals("Fields must be mutually exclusive", violations.iterator().next().getMessage());

    }

    @Test
    void optional_ok() {
        var data = new Data(null, Optional.of(1), null, null, null, null);
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

    @Test
    void optional_empty() {
        var data = new Data(1, Optional.empty(), null, null, null, null);
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

    @Test
    void collection_ok() {
        var data = new Data(null, null, List.of(1), null, null, null);
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

    @Test
    void collection_empty() {
        var data = new Data(1, null, Collections.emptyList(), null, null, null);
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

    @Test
    void map_ok() {
        var data = new Data(null, null,null, Map.of(1, 1), null, null);
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

    @Test
    void map_empty() {
        var data = new Data(1, null,null, Collections.emptyMap(), null, null);
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

    @Test
    void iterator_ok() {
        var data = new Data(null, null,null, null, List.of(new Object()).iterator(), null);
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

    @Test
    void iterator_empty() {
        var data = new Data(1, null,null, null, Collections.emptyIterator(), null);
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

    @Test
    void array_ok() {
        var data = new Data(null, null,null, null, null, new Object[]{1});
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

    @Test
    void array_empty() {
        var data = new Data(1, null,null, null, null, new Object[]{});
        var violations = validator.validate(data);
        assertEquals(0, violations.size());
    }

}