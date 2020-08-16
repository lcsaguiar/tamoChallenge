package com.tamo.calendar.dao;

import com.tamo.calendar.model.client.Candidate;
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
public class CandidateDaoTestIntegration {
    @Autowired
    private CandidateDao candidateDao;

    @Test
    public void saveCandidateAndGetList() throws Exception {
        Candidate candidate = new Candidate("test", "test@test.com");
        candidateDao.saveCandidate(candidate);

        List<Candidate> candidates = candidateDao.getCandidatesList();

        assertEquals("The candidates length is wrong!", 1, candidates.size());
        assertEquals("The candidate is not the expected", candidates.get(0), candidate);
    }

    @Test
    public void saveCandidateAndGetById() throws Exception {
        Candidate candidate = new Candidate("test", "test@test.com");
        candidateDao.saveCandidate(candidate);

        Candidate actual = candidateDao.getCandidateById(candidate.getId().toString());

        assertEquals("The candidate is not the expected",  candidate, actual);
    }
}
