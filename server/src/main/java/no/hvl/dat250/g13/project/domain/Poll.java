package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int index;

    private String text;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    private Survey survey;

    // Default constructor (required by JPA)
    public Poll() {}

    // All-arguments constructor
    public Poll(Long id, int index, String text, List<Option> options) {
        setId(id);
        setIndex(index);
        setText(text);
        setOptions(options);
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

    public void setIndex(int index) {
        this.index = index;
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
        if (this.options != null)
            this.options.forEach(option -> option.setPoll(null));
        if (options != null)
            options.forEach(option -> option.setPoll(this));
        this.options.clear();
        this.options.addAll(options);
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return index == poll.index && Objects.equals(id, poll.id) && Objects.equals(text, poll.text) && Objects.equals(options, poll.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, index, text, options);
    }
}
