package com.tamo.calendar.utils;

import com.tamo.calendar.model.interview.Duration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

@Component
public class InterviewHourInterval implements InterviewInterval {
    private static final int HOURS = 1;

    private boolean overlapDates(LocalDateTime startA, LocalDateTime endA, LocalDateTime startB, LocalDateTime endB) {
        return !startA.isAfter(endB) && !endA.minusHours(1).isBefore(startB);
    }
    public List<Duration> calculateInterviews(List<Duration> client1, List<Duration> client2) {
        // remove past durations
        client1.removeIf(duration -> (duration.getEnd().minusHours(HOURS).isBefore(LocalDateTime.now())));
        client2.removeIf(duration -> (duration.getEnd().minusHours(HOURS).isBefore(LocalDateTime.now())));

        return calculateInterviewsAux(client1, client2);
    }

    private List<Duration> calculateInterviewsAux(List<Duration> client1, List<Duration> client2) {
        List<Duration> interviews = new LinkedList<>();
        for(Duration duration1: client1) {
            for(Duration duration2: client2) {
                if(overlapDates(duration1.getStart(), duration1.getEnd(), duration2.getStart(), duration2.getEnd())) {
                    LocalDateTime start = duration1.getStart().isAfter(duration2.getStart()) ?
                            duration1.getStart() :
                            duration2.getStart();

                    // start duration must be after than now
                    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                    start = start.isBefore(now) ? now : start;

                    LocalDateTime end = duration1.getEnd().isBefore(duration2.getEnd()) ?
                            duration1.getEnd() :
                            duration2.getEnd();

                    addInterviews(interviews, start, end);
                }
            }
        }
        return interviews;
    }

    private void addInterviews(List<Duration> interviews, LocalDateTime start, LocalDateTime end) {
        LocalDateTime endInterval = start.plusHours(HOURS);
        while(!endInterval.isAfter(end)) {
            interviews.add(new Duration(start, endInterval));
            start = endInterval;
            endInterval = endInterval.plusHours(1);
        }
    }
}
