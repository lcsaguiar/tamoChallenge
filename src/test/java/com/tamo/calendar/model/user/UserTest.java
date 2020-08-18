package com.tamo.calendar.model.user;

import com.tamo.calendar.model.interview.Availability;
import com.tamo.calendar.model.interview.Interview;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void returnDatesList() {
        Availability availability = new Availability();
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        availability.setStart(start);
        availability.setEnd(end);
        user.setAvailabilities(List.of(availability));

        List<Interview> datesList =  user.returnDatesList();

        List<Interview> expected = List.of(new Interview(start, end));

        assertEquals("The array length is not what was expected!", 1, datesList.size());
        assertEquals("The result is not the expected", datesList.get(0), expected.get(0));

    }
}