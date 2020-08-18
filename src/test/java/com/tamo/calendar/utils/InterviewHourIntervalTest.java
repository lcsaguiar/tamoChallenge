package com.tamo.calendar.utils;

import com.tamo.calendar.model.interview.Duration;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InterviewHourIntervalTest {
    private List<Duration> list1;
    private List<Duration> list2;
    private InterviewHourInterval interviewHourInterval;

    @Before
    public void setUp() {
        interviewHourInterval = new InterviewHourInterval();
    }

    @Test
    public void overlapDurationWithOneInterview() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = start.plusHours(1);
        list1 = new LinkedList<>(List.of(new Duration(start, end)));
        list2 = new LinkedList<>(List.of(new Duration(start, end)));

        List<Duration> interviews = interviewHourInterval.calculateInterviews(list1, list2);
        List<Duration> expected = new LinkedList<>(List.of(
                new Duration(start, end)));

        assertEquals("The array length is not what was expected!", 1, interviews.size());
        assertEquals("The result is not the expected", expected.get(0), interviews.get(0));
    }

    @Test
    public void overlapDurationWithMultipleInterviews() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = start.plusHours(2);
        list1 = new LinkedList<>(List.of(new Duration(start, end)));
        list2 = new LinkedList<>(List.of(new Duration(start, end)));

        List<Duration> interviews =  interviewHourInterval.calculateInterviews(list1, list2);
        List<Duration> expected = new LinkedList<>(List.of(
                new Duration(start, start.plusHours(1)),
                new Duration(start.plusHours(1), end)));

        assertEquals("The array length is not what was expected!", 2, interviews.size());
        assertEquals("The result is not the expected", expected.get(0), interviews.get(0));
        assertEquals("The result is not the expected", expected.get(1), interviews.get(1));
    }

    @Test
    public void noOverlapDuration() {
        LocalDateTime start1 = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end1 = LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime start2 = LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end2 = LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.HOURS);
        list1 = new LinkedList<>(List.of(new Duration(start1, end1)));
        list2 = new LinkedList<>(List.of(new Duration(start2, end2)));

        List<Duration> interviews = interviewHourInterval.calculateInterviews(list1, list2);

        assertEquals("The array length is not what was expected!", 0, interviews.size());
    }

    @Test
    public void overlapDurationWithOldDates() {
        LocalDateTime start = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = start.plusHours(1);
        list1 = new LinkedList<>(List.of(new Duration(start, end)));
        list2 = new LinkedList<>(List.of(new Duration(start, end)));

        List<Duration> interviews = interviewHourInterval.calculateInterviews(list1, list2);

        assertEquals("The array length is not what was expected!", 0, interviews.size());
    }

    @Test
    public void overlapDurationWithSomePartOldThanNow() {
        LocalDateTime start = LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = start.plusHours(3);
        list1 = new LinkedList<>(List.of(new Duration(start, end)));
        list2 = new LinkedList<>(List.of(new Duration(start, end)));

        List<Duration> interviews = interviewHourInterval.calculateInterviews(list1, list2);

        List<Duration> expected = new LinkedList<>(List.of(
                new Duration(start.plusHours(1), start.plusHours(2)),
                new Duration(start.plusHours(2), end)));

        assertEquals("The array length is not what was expected!", 2, interviews.size());
        assertEquals("The result is not the expected",expected.get(0), interviews.get(0));
        assertEquals("The result is not the expected", expected.get(1), interviews.get(1));
    }
}