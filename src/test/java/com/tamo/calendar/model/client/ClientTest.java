package com.tamo.calendar.model.client;

import com.tamo.calendar.model.interview.Availability;
import com.tamo.calendar.model.interview.Duration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClientTest {
    private Client client;

    @Before
    public void setUp() {
        client = new Client();
    }

    @Test
    public void returnDatesList() {
        Availability availability = new Availability();
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        availability.setStart_duration(start);
        availability.setEnd_duration(end);
        client.setAvailabilities(List.of(availability));

        List<Duration> datesList =  client.returnDatesList();

        List<Duration> expected = List.of(new Duration(start, end));

        assertEquals("The array length is not what was expected!", 1, datesList.size());
        assertEquals("The result is not the expected", datesList.get(0), expected.get(0));

    }

    @Test
    public void returnDatesListWithDatesBeforeNow() {
        Availability availability = new Availability();
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().minusHours(1);
        availability.setStart_duration(start);
        availability.setEnd_duration(end);
        client.setAvailabilities(List.of(availability));

        List<Duration> datesList =  client.returnDatesList();

        assertEquals("The array length is not what was expected!", 0, datesList.size());

    }
}