package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class OptionRef {

    @EmbeddedId
    private OptionRefId id;
    public record OptionRefId(Long surveyId, Long optionId) implements Serializable { }

    // Default constructor (required by JPA)
    public OptionRef() {}

    // All-arguments constructor
    public OptionRef(OptionRefId id) {
        this.id = id;
    }

    public OptionRef(Long surveyId, Long optionId) {
        this.id = new OptionRefId(surveyId, optionId);
    }

    // Getters and Setters
    public OptionRefId getId() {
        return id;
    }

    public void setId(OptionRefId id) {
        this.id = id;
    }

    public Long getOptionId() {
        return id.optionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionRef optionRef = (OptionRef) o;
        return Objects.equals(id, optionRef.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
