package no.hvl.dat250.g13.project.domain;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ElementCollection;

@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<Long> options;

    @ManyToOne
    private User voter;

    @ManyToOne
    private Survey survey;

    // Default constructor (required by JPA)
    public Vote() {}

    // All-arguments constructor
    public Vote(Long id, List<Long> options, User voter, Survey survey) {
        this.id = id;
        this.options = options;
        this.voter = voter;
        this.survey = survey;
    }
}
