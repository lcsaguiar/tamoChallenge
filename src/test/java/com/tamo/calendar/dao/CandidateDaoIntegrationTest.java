package com.tamo.calendar.dao;

import com.tamo.calendar.exceptions.CandidateNotFoundException;
import com.tamo.calendar.model.user.Candidate;
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
public class CandidateDaoIntegrationTest {
    @Autowired
    private CandidateDao candidateDao;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresSqlContainer.getInstance();

    @Test
    public void saveCandidateAndGetList() {
        Candidate candidate = new Candidate("test", "test@test.com");
        candidateDao.saveCandidate(candidate);

        List<Candidate> candidates = candidateDao.getCandidatesList();

        assertEquals("The candidates length is wrong!", 1, candidates.size());
        assertEquals("The candidate is not the expected", candidates.get(0), candidate);
    }

    @Test
    public void saveCandidateAndGetById() {
        Candidate candidate = new Candidate("test", "test@test.com");
        candidateDao.saveCandidate(candidate);

        Candidate actual = candidateDao.getCandidateById(candidate.getId().toString());

        assertEquals("The candidate is not the expected",  candidate, actual);
    }

    @Test(expected = CandidateNotFoundException.class)
    public void saveCandidateAndGetByDifferentId() {
        Candidate candidate = new Candidate("test", "test@test.com");
        candidateDao.saveCandidate(candidate);
        Long wrongId = candidate.getId() + 1L;

        candidateDao.getCandidateById(wrongId.toString());
    }
}
