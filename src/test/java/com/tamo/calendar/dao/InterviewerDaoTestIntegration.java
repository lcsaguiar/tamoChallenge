package com.tamo.calendar.dao;

import com.tamo.calendar.model.client.Interviewer;
import com.tamo.calendar.model.client.Interviewer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class InterviewerDaoTestIntegration {
    @Autowired
    private InterviewerDao interviewerDao;

    @Test
    public void saveInterviewerAndGetList() throws Exception {
        Interviewer interviewer = new Interviewer("test", "test@test.com");
        interviewerDao.saveInterviewer(interviewer);

        List<Interviewer> interviewers = interviewerDao.getInterviewersList();

        assertEquals("The interviewers length is wrong!", 1, interviewers.size());
        assertEquals("The interviewer is not the expected", interviewer, interviewers.get(0));
    }

    @Test
    public void saveInterviewerAndGetById() throws Exception {
        Interviewer interviewer = new Interviewer("test", "test@test.com");
        interviewerDao.saveInterviewer(interviewer);

        Interviewer actual = interviewerDao.getInterviewerById(interviewer.getId().toString());

        assertEquals("The interviewer is not the expected", interviewer, actual);
    }
}
