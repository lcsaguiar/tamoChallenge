package com.tamo.calendar.dao;

import com.tamo.calendar.exceptions.InterviewerNotFoundException;
import com.tamo.calendar.exceptions.UserNotFoundException;
import com.tamo.calendar.model.user.User;
import com.tamo.calendar.model.user.Interviewer;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserDaoIntegrationTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private InterviewerDao interviewerDao;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresSqlContainer.getInstance();

    @Test
    public void saveUserAndGetById() {
        Interviewer interviewerTest = new Interviewer("test", "test@test.com");
        interviewerDao.saveInterviewer(interviewerTest);

        User expected = userDao.getUserById(interviewerTest.getId().toString());

        assertEquals("The user is not the expected", expected, interviewerTest);
    }

    @Test(expected = UserNotFoundException.class)
    public void saveUserGetByDifferentId() {
        Interviewer interviewer = new Interviewer("test", "test@test.com");
        interviewerDao.saveInterviewer(interviewer);
        Long wrongId = interviewer.getId() + 1L;

        userDao.getUserById(wrongId.toString());
    }
}
