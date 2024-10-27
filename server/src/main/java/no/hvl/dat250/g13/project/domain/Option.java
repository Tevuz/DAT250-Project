package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int index;

    private String text;

    @ManyToOne
    private Poll poll;

    // Default constructor (required by JPA)
    public Option() {}

    // All-arguments constructor
    public Option(Long id, int order, String text, Poll poll) {
        this.id = id;
        this.index = order;
        this.text = text;
        this.poll = poll;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return index == option.index && Objects.equals(id, option.id) && Objects.equals(text, option.text) && Objects.equals(poll, option.poll);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, index, text, poll);
    }
}
