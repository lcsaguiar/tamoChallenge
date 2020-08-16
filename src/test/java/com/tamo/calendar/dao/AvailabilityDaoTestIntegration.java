package com.tamo.calendar.dao;

import com.tamo.calendar.model.client.Interviewer;
import com.tamo.calendar.model.interview.Availability;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AvailabilityDaoTestIntegration {

    @Autowired
    private AvailabilityDao availabilityDao;

    @Autowired
    private InterviewerDao interviewerDao;

    @Test
    public void saveAvailabilityAndGetList() throws Exception {
        Interviewer interviewer = new Interviewer("test", "test@test.com");
        interviewerDao.saveInterviewer(interviewer);
        Availability availability = new Availability(LocalDateTime.now(), LocalDateTime.now(), interviewer);
        availabilityDao.saveAvailability(availability);

        List<Availability> availabilities = availabilityDao.getAvailabilityList();

        assertEquals("The availabilities length is wrong!", 1, availabilities.size());
        assertEquals("The availability is not the expected", availability, availabilities.get(0));
    }
}
