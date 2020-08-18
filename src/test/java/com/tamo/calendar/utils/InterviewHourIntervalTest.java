package com.tamo.calendar.utils;

import com.tamo.calendar.model.interview.Interview;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InterviewHourIntervalTest {
    private List<Interview> list1;
    private List<Interview> list2;
    private InterviewHourInterval interviewHourInterval;

    @Before
    public void setUp() {
        interviewHourInterval = new InterviewHourInterval();
    }

    @Test
    public void overlapInterviewWithOneInterview() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = start.plusHours(1);
        list1 = new LinkedList<>(List.of(new Interview(start, end)));
        list2 = new LinkedList<>(List.of(new Interview(start, end)));

        List<Interview> interviews = interviewHourInterval.calculateInterviews(list1, list2);
        List<Interview> expected = new LinkedList<>(List.of(
                new Interview(start, end)));

        assertEquals("The array length is not what was expected!", 1, interviews.size());
        assertEquals("The result is not the expected", expected.get(0), interviews.get(0));
    }

    @Test
    public void overlapInterviewWithMultipleInterviews() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = start.plusHours(2);
        list1 = new LinkedList<>(List.of(new Interview(start, end)));
        list2 = new LinkedList<>(List.of(new Interview(start, end)));

        List<Interview> interviews =  interviewHourInterval.calculateInterviews(list1, list2);
        List<Interview> expected = new LinkedList<>(List.of(
                new Interview(start, start.plusHours(1)),
                new Interview(start.plusHours(1), end)));

        assertEquals("The array length is not what was expected!", 2, interviews.size());
        assertEquals("The result is not the expected", expected.get(0), interviews.get(0));
        assertEquals("The result is not the expected", expected.get(1), interviews.get(1));
    }

    @Test
    public void noOverlapInterview() {
        LocalDateTime start1 = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end1 = LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime start2 = LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end2 = LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.HOURS);
        list1 = new LinkedList<>(List.of(new Interview(start1, end1)));
        list2 = new LinkedList<>(List.of(new Interview(start2, end2)));

        List<Interview> interviews = interviewHourInterval.calculateInterviews(list1, list2);

        assertEquals("The array length is not what was expected!", 0, interviews.size());
    }

    @Test
    public void overlapInterviewWithOldDates() {
        LocalDateTime start = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = start.plusHours(1);
        list1 = new LinkedList<>(List.of(new Interview(start, end)));
        list2 = new LinkedList<>(List.of(new Interview(start, end)));

        List<Interview> interviews = interviewHourInterval.calculateInterviews(list1, list2);

        assertEquals("The array length is not what was expected!", 0, interviews.size());
    }

    @Test
    public void overlapInterviewWithSomePartOldThanNow() {
        LocalDateTime start = LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = start.plusHours(3);
        list1 = new LinkedList<>(List.of(new Interview(start, end)));
        list2 = new LinkedList<>(List.of(new Interview(start, end)));

        List<Interview> interviews = interviewHourInterval.calculateInterviews(list1, list2);

        List<Interview> expected = new LinkedList<>(List.of(
                new Interview(start.plusHours(1), start.plusHours(2)),
                new Interview(start.plusHours(2), end)));

        assertEquals("The array length is not what was expected!", 2, interviews.size());
        assertEquals("The result is not the expected",expected.get(0), interviews.get(0));
        assertEquals("The result is not the expected", expected.get(1), interviews.get(1));
    }
}