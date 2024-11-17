package no.hvl.dat250.g13.project.service.data.validation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.*;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

public interface Validate {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @JsonIgnore
    default boolean isValid() {
        return validator.validate(this).isEmpty();
    }

    default void validate() throws BindException {
         var violations = validator.validate(this);
         if (!violations.isEmpty()) {
             var exception = new BindException(this, "DTO");
             violations.stream()
                     .map(violation -> new FieldError("DTO", violation.getPropertyPath().toString(), violation.getMessage()))
                     .forEach(exception::addError);
             throw exception;
         }
    }
}
