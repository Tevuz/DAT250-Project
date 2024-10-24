package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class OptionRef {

    @EmbeddedId
    private OptionRefId id;
    public record OptionRefId(Long voteId, Long optionId) implements Serializable { }

    // Default constructor (required by JPA)
    public OptionRef() {}

    // All-arguments constructor
    public OptionRef(OptionRefId id) {
        this.id = id;
    }

    public OptionRef(Long voteId, Long optionId) {
        this.id = new OptionRefId(voteId, optionId);
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
}
