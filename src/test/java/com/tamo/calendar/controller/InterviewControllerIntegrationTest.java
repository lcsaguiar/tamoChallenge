package com.tamo.calendar.controller;

import com.tamo.calendar.CalendarApplication;
import com.tamo.calendar.dao.CandidateDao;
import com.tamo.calendar.dao.InterviewerDao;
import com.tamo.calendar.dao.PostgresSqlContainer;
import com.tamo.calendar.model.interview.Availability;
import com.tamo.calendar.model.user.Candidate;
import com.tamo.calendar.model.user.Interviewer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment=SpringBootTest.WebEnvironment.MOCK,
        classes = CalendarApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class InterviewControllerIntegrationTest {
    private Candidate candidate;

    private Interviewer interviewer;

    private LocalDateTime start;

    private LocalDateTime end;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CandidateDao candidateDao;

    @Autowired
    private InterviewerDao interviewerDao;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresSqlContainer.getInstance();

    @Before
    public void setUp() {
        start = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS);
        end = LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.HOURS);
        candidate = new Candidate("test", "test@test.com");
        interviewer = new Interviewer("test", "test@test.com");
        List<Availability> availability = List.of(new Availability(start, end));
        candidate.setAvailabilities(availability);
        interviewer.setAvailabilities(availability);
        candidateDao.saveCandidate(candidate);
        interviewerDao.saveInterviewer(interviewer);
    }

    @Test
    public void getInterviewWithOneInterviewer() throws Exception {
        mockMvc.perform(get("/interview/" + candidate.getId().toString()  + "?interviewerId=" + interviewer.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].start").value(start.toString()))
                .andExpect(jsonPath("$[0].end").value(end.toString()));
    }

    @Test
    public void getInterviewWithInvalidCandidate() throws Exception {
        Long wrongId = candidate.getId() + 1L;

        mockMvc.perform(get("/interview/" + wrongId.toString() + "?interviewerId=" + interviewer.getId().toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").value("Candidate with id " + wrongId + " not found"));
    }

    @Test
    public void getInterviewWithInvalidInterviewer() throws Exception {
        Long wrongId = interviewer.getId() + 1L;

        mockMvc.perform(get("/interview/" + candidate.getId().toString()  + "?interviewerId=" + wrongId.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").value("Interviewer with id " + wrongId + " not found"));
    }
}
