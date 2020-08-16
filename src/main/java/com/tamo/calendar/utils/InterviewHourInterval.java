package com.tamo.calendar.utils;

import com.tamo.calendar.model.interview.Duration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public class InterviewHourInterval implements InterviewInterval {
    private final static int hours = 1;

    private boolean overlapDates(LocalDateTime startA, LocalDateTime endA, LocalDateTime startB, LocalDateTime endB) {
        return !startA.isAfter(endB) && !endA.isBefore(startB);
    }
    public List<Duration> calculateInterviews(List<Duration> client1, List<Duration> client2) {
        List<Duration> interviews = new LinkedList<>();
        for(Duration a: client1) {
            for(Duration a2: client2) {
                if(overlapDates(

                        a.getStart(),
                        a.getEnd(),
                        a2.getStart(),
                        a2.getEnd())) {

                    LocalDateTime start = a.getStart().isAfter(a2.getStart()) ?
                            a.getStart() :
                            a2.getStart();
                    LocalDateTime end = a.getEnd().isBefore(a2.getEnd()) ?
                            a.getEnd() :
                            a2.getEnd();
                    addInterviews(interviews, start, end);
                }
            }
        }
        return interviews;
    }

    private void addInterviews(List<Duration> interviews, LocalDateTime start, LocalDateTime end) {
        LocalDateTime endInterval = start.plusHours(hours);
        while(!endInterval.isAfter(end)) {
            interviews.add(new Duration(start, endInterval));
            start = endInterval;
            endInterval = endInterval.plusHours(1);
        }
    }
}
