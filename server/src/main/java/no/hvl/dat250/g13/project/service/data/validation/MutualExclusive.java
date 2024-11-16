package no.hvl.dat250.g13.project.service.data.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {MutualExclusiveValidator.class})
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MutualExclusive {

    String[] fields();

    String message() default "Fields must be mutually exclusive";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
