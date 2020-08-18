package com.tamo.calendar.model.interview;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tamo.calendar.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "availabilities")
public class Availability {
    @Transient
    private final String format = "uuuu-MM-dd'T'HH:mm";

    @Id
    @GeneratedValue
    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(required = true, example = "2021-12-31T01:00")
    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = format)
    @Column(name="start_availability")
    private LocalDateTime start;

    @ApiModelProperty(required = true, example = "2021-12-31T10:00")
    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = format)
    @Column(name="end_availability")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Availability() {
    }

    public Availability(LocalDateTime start, LocalDateTime end, User user) {
        this.start = start;
        this.end = end;
        this.user = user;
    }

    public Availability(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setUser(User user) { this.user = user; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, user);
    }

    @JsonBackReference
    public User getUser() {
        return user;
    }
}
