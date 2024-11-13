package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;
import no.hvl.dat250.g13.project.domain.Identifiers.OptionKey;

import java.util.Objects;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private OptionKey id;

    private int index;

    private String text;

    @ManyToOne
    @JoinColumn
    private Poll poll;

    // Default constructor (required by JPA)
    public Option() {}

    // All-arguments constructor
    public Option(OptionKey id, int index, String text) {
        setId(id);
        setIndex(index);
        setText(text);
    }

    // Getters and Setters
    public OptionKey getId() {
        return id;
    }

    public void setId(OptionKey id) {
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
        return index == option.index && Objects.equals(id, option.id) && Objects.equals(text, option.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, index, text);
    }
}
