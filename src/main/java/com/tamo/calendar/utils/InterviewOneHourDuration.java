package com.tamo.calendar.utils;

import com.tamo.calendar.model.interview.Interview;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InterviewOneHourDuration implements InterviewDuration {
    private static final int HOURS = 1;

    private boolean overlapDates(LocalDateTime startA, LocalDateTime endA, LocalDateTime startB, LocalDateTime endB) {
        return !startA.isAfter(endB) && !endA.minusHours(1).isBefore(startB);
    }

    public List<Interview> calculateInterviews(List<Interview> user1, List<Interview> user2) {
        // put dates according to the valid slots
        user1 = user1.stream().map(this::getInterviewSlot).collect(Collectors.toList());
        user2 = user2.stream().map(this::getInterviewSlot).collect(Collectors.toList());

        // remove past dates
        user1.removeIf(interview -> (interview.getEnd().minusHours(HOURS).isBefore(LocalDateTime.now())));
        user2.removeIf(interview -> (interview.getEnd().minusHours(HOURS).isBefore(LocalDateTime.now())));

        // calculate the interviews
        return calculateInterviewsAux(user1, user2);
    }

    private List<Interview> calculateInterviewsAux(List<Interview> user1, List<Interview> user2) {
        List<Interview> interviews = new LinkedList<>();
        for(Interview interview1 : user1) {
            for(Interview interview2 : user2) {
                if(overlapDates(
                        interview1.getStart(),
                        interview1.getEnd(),
                        interview2.getStart(),
                        interview2.getEnd())
                ) {
                    // check when the interview must start
                    LocalDateTime start = interview1.getStart().isAfter(interview2.getStart()) ?
                            interview1.getStart() :
                            interview2.getStart();

                    // start interview must be after than now
                    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
                    start = start.isBefore(now) ? now : start;

                    // check when the interview must end
                    LocalDateTime end = interview1.getEnd().isBefore(interview2.getEnd()) ?
                            interview1.getEnd() :
                            interview2.getEnd();

                    addInterviews(interviews, start, end);
                }
            }
        }

        return interviews;
    }

    private void addInterviews(List<Interview> interviews, LocalDateTime start, LocalDateTime end) {
        LocalDateTime endInterval = start.plusHours(HOURS);
        while(!endInterval.isAfter(end)) {
            interviews.add(new Interview(start, endInterval));
            start = endInterval;
            endInterval = endInterval.plusHours(1);
        }
    }

    private Interview getInterviewSlot(Interview interview) {
        if (interview.getStart().getMinute() > 0 ) {
            interview.setStart(interview.getStart().plusHours(1).truncatedTo(ChronoUnit.HOURS));
        }
        if(interview.getEnd().getMinute() > 0) {
            interview.setEnd(interview.getEnd().minusHours(1).truncatedTo(ChronoUnit.HOURS));
        }

        return interview;
    }
}
