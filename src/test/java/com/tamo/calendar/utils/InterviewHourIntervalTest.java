package com.tamo.calendar.utils;

import com.tamo.calendar.model.interview.Duration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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
        list1 = new LinkedList<>(List.of(new Duration(LocalDateTime.parse("2020-09-16T08:00:00"), LocalDateTime.parse("2020-09-16T09:00:00"))));
        list2 = new LinkedList<>(List.of(new Duration(LocalDateTime.parse("2020-09-16T08:00:00"), LocalDateTime.parse("2020-09-16T09:00:00"))));

        List<Duration> interviews = interviewHourInterval.calculateInterviews(list1, list2);
        List<Duration> expected = new LinkedList<>(List.of(
                new Duration(LocalDateTime.parse("2020-09-16T08:00:00"), LocalDateTime.parse("2020-09-16T09:00:00"))));

        Assert.assertEquals("The array length is not what was expected!", 1, interviews.size());
        Assert.assertEquals("The result is not the expected", interviews.get(0), expected.get(0));
    }

    @Test
    public void overlapDurationWithMultipleInterviews() {
        list1 = new LinkedList<>(List.of(new Duration(LocalDateTime.parse("2020-09-16T08:00:00"), LocalDateTime.parse("2020-09-16T10:00:00"))));
        list2 = new LinkedList<>(List.of(new Duration(LocalDateTime.parse("2020-09-16T08:00:00"), LocalDateTime.parse("2020-09-16T10:00:00"))));

        List<Duration> interviews =  interviewHourInterval.calculateInterviews(list1, list2);
        List<Duration> expected = new LinkedList<>(List.of(
                new Duration(LocalDateTime.parse("2020-09-16T08:00:00"), LocalDateTime.parse("2020-09-16T09:00:00")),
                new Duration(LocalDateTime.parse("2020-09-16T09:00:00"), LocalDateTime.parse("2020-09-16T10:00:00"))));

        Assert.assertEquals("The array length is not what was expected!", 2, interviews.size());
        Assert.assertEquals("The result is not the expected", interviews.get(0), expected.get(0));
        Assert.assertEquals("The result is not the expected", interviews.get(1), expected.get(1));
    }

    @Test
    public void noOverlapDuration() {
        list1 = new LinkedList<>(List.of(new Duration(LocalDateTime.parse("2020-09-16T07:00:00"), LocalDateTime.parse("2020-09-16T08:00:00"))));
        list2 = new LinkedList<>(List.of(new Duration(LocalDateTime.parse("2020-09-16T08:00:00"), LocalDateTime.parse("2020-09-16T09:00:00"))));

        List<Duration> interviews = interviewHourInterval.calculateInterviews(list1, list2);

        Assert.assertEquals("The array length is not what was expected!", 0, interviews.size());
    }

    @Test
    public void overlapDurationWithLessThanAnHour() {
        list1 = new LinkedList<>(List.of(new Duration(LocalDateTime.parse("2020-09-16T07:00:00"), LocalDateTime.parse("2020-09-16T08:59:00"))));
        list2 = new LinkedList<>(List.of(new Duration(LocalDateTime.parse("2020-09-16T08:00:00"), LocalDateTime.parse("2020-09-16T09:00:00"))));

        List<Duration> interviews = interviewHourInterval.calculateInterviews(list1, list2);

        Assert.assertEquals("The array length is not what was expected!", 0, interviews.size());
    }
}