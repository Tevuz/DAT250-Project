package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int index;

    @ManyToOne
    private Survey survey;

    private String text;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    private List<Option> options;

    // Default constructor (required by JPA)
    public Poll() {}

    // All-arguments constructor
    public Poll(Long id, int order, Survey survey, String text, List<Option> options) {
        this.id = id;
        this.index = order;
        this.survey = survey;
        this.text = text;
        this.options = options;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int order) {
        this.index = order;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
