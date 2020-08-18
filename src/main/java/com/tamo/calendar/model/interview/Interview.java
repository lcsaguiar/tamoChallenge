package com.tamo.calendar.model.interview;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Objects;

public class Interview {
    @Transient
    private final String format = "uuuu-MM-dd'T'HH:mm";

    @JsonFormat(pattern = format)
    private LocalDateTime start;

    @JsonFormat(pattern = format)
    private LocalDateTime end;

    public Interview(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interview)) return false;
        Interview interview = (Interview) o;
        return Objects.equals(start, interview.start) &&
                Objects.equals(end, interview.end);
    }
}
