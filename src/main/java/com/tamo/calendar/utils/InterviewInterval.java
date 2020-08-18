package com.tamo.calendar.utils;

import com.tamo.calendar.model.interview.Interview;

import java.util.List;

public interface InterviewInterval {
    List<Interview> calculateInterviews(List<Interview> user1, List<Interview> user2);
}
