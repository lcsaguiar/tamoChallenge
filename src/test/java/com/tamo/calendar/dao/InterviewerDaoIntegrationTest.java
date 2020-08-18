package com.tamo.calendar.dao;

import com.tamo.calendar.exceptions.CandidateNotFoundException;
import com.tamo.calendar.exceptions.InterviewerNotFoundException;
import com.tamo.calendar.model.client.Candidate;
import com.tamo.calendar.model.client.Interviewer;
import com.tamo.calendar.model.client.Interviewer;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class InterviewerDaoIntegrationTest {
    @Autowired
    private InterviewerDao interviewerDao;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresSqlContainer.getInstance();

    @Test
    public void saveInterviewerAndGetList() {
        Interviewer interviewer = new Interviewer("test", "test@test.com");
        interviewerDao.saveInterviewer(interviewer);

        List<Interviewer> interviewers = interviewerDao.getInterviewersList();

        assertEquals("The interviewers length is wrong!", 1, interviewers.size());
        assertEquals("The interviewer is not the expected", interviewer, interviewers.get(0));
    }

    @Test
    public void saveInterviewerAndGetById() {
        Interviewer interviewer = new Interviewer("test", "test@test.com");
        interviewerDao.saveInterviewer(interviewer);

        Interviewer actual = interviewerDao.getInterviewerById(interviewer.getId().toString());

        assertEquals("The interviewer is not the expected", interviewer, actual);
    }

    @Test(expected = InterviewerNotFoundException.class)
    public void saveCandidateAndGetByDifferentId() {
        Interviewer interviewer = new Interviewer("test", "test@test.com");
        interviewerDao.saveInterviewer(interviewer);
        Long wrongId = interviewer.getId() + 1L;
        interviewerDao.getInterviewerById(wrongId.toString());
    }
}
