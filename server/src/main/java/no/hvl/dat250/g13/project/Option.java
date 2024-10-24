package no.hvl.dat250.g13.project;

import jakarta.persistence.*;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int order;

    private int voteCount;

    @ManyToOne
    private Poll poll;

    // Default constructor (required by JPA)
    public Option() {}

    // All-arguments constructor
    public Option(Long id, int order, int voteCount, Poll poll) {
        this.id = id;
        this.order = order;
        this.voteCount = voteCount;
        this.poll = poll;
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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
