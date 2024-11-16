package no.hvl.dat250.g13.project.service.data.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.*;

public class MutualExclusiveValidator implements ConstraintValidator<MutualExclusive, Object> {

    private String[] fields;

    @Override
    public void initialize(MutualExclusive annotation) {
        fields = annotation.fields();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null)
            return true;

        int count = 0;

        for (String name : fields) {
            try {
                Field field = object.getClass().getDeclaredField(name);
                field.setAccessible(true);
                Object value = field.get(object);

                switch (value) {
                    case null -> { }
                    case Optional<?> optional -> { if (optional.isPresent()) count++; }
                    case Collection<?> collection-> { if (!collection.isEmpty()) count++; }
                    case Map<?, ?> map -> { if (!map.isEmpty()) count++; }
                    case Iterator<?> iterator -> { if (iterator.hasNext()) count++; }
                    case Object[] array -> { if (array.length != 0) count++; }
                    default -> { count++; }
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return count == 1;
    }
}
