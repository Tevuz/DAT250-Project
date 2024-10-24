package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int order;

    private String text;

    // Default constructor (required by JPA)
    public Option() {}

    // All-arguments constructor
    public Option(Long id, int order, String text) {
        this.id = id;
        this.order = order;
        this.text = text;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
