package com.tamo.calendar.model.interview;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tamo.calendar.model.client.Client;
import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Availability {
    @Id
    @GeneratedValue
    private Long id;

    @FutureOrPresent
    private LocalDateTime start_duration;
    @FutureOrPresent
    private LocalDateTime end_duration;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;

    public Availability() {
    }

    public Availability(LocalDateTime start_duration, LocalDateTime end_duration, Client client) {
        this.start_duration = start_duration;
        this.end_duration = end_duration;
        this.client = client;
    }

    public Availability(LocalDateTime start_duration, LocalDateTime end_duration) {
        this.start_duration = start_duration;
        this.end_duration = end_duration;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStart_duration() {
        return start_duration;
    }

    public void setStart_duration(LocalDateTime start_duration) {
        this.start_duration = start_duration;
    }

    public LocalDateTime getEnd_duration() {
        return end_duration;
    }

    public void setEnd_duration(LocalDateTime end_duration) {
        this.end_duration = end_duration;
    }

    public void setClient(Client client) { this.client = client; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(start_duration, that.start_duration) &&
                Objects.equals(end_duration, that.end_duration) &&
                Objects.equals(client, that.client);
    }

    @JsonBackReference
    public Client getClient() {
        return client;
    }
}
