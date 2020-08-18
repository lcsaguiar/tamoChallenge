package com.tamo.calendar.dao;

import com.tamo.calendar.model.client.Client;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ClientDaoIntegrationTest {
    @Autowired
    private ClientDao clientDao;

    @Autowired
    private InterviewerDao interviewerDao;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresSqlContainer.getInstance();

    @Test
    public void saveClientAndGetById() {
        Interviewer interviewerTest = new Interviewer("test", "test@test.com");
        interviewerDao.saveInterviewer(interviewerTest);

        Client expected = clientDao.getClientById(interviewerTest.getId().toString());

        assertEquals("The client is not the expected", expected, interviewerTest);
    }
}
