package com.tamo.calendar.utils;

import com.tamo.calendar.model.interview.Duration;

import java.util.List;

public interface InterviewInterval {
    List<Duration> calculateInterviews(List<Duration> client1, List<Duration> client2);
}
