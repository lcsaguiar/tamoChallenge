package com.tamo.calendar.model.interview;

import java.time.LocalDateTime;
import java.util.Objects;

public class Duration {
    private LocalDateTime start;
    private LocalDateTime end;

    public Duration(LocalDateTime start, LocalDateTime end) {
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
        if (!(o instanceof Duration)) return false;
        Duration duration = (Duration) o;
        return Objects.equals(start, duration.start) &&
                Objects.equals(end, duration.end);
    }
}
