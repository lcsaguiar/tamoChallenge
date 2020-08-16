package com.tamo.calendar.controller;

import com.tamo.calendar.dao.CandidateDao;
import com.tamo.calendar.dao.InterviewerDao;
import com.tamo.calendar.model.client.Candidate;
import com.tamo.calendar.model.client.Interviewer;
import com.tamo.calendar.model.interview.Availability;
import com.tamo.calendar.model.interview.Duration;
import com.tamo.calendar.utils.InterviewHourInterval;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(InterviewController.class)
public class InterviewControllerTest {
    private Candidate candidate;

    private Interviewer interviewer;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateDao candidateDao;

    @MockBean
    private InterviewerDao interviewerDao;

    @MockBean
    private InterviewHourInterval interviewHourInterval;

    @Before
    public void setUp() {
        List<Availability> availabilities = List.of(new Availability(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(5)));
        candidate = new Candidate(1L, "test", "test@test.com", availabilities);
        interviewer = new Interviewer(2L,"test", "test@test.com", availabilities);

        given(candidateDao.getCandidateById(candidate.getId().toString())).willReturn(candidate);
        given(interviewerDao.getInterviewerById(interviewer.getId().toString())).willReturn(interviewer);
        given(interviewHourInterval.calculateInterviews(any(), any())).willReturn(List.of( new Duration(LocalDateTime.now(), LocalDateTime.now())));


    }
    @Test
    public void getInterviewWithOneInterviewer() throws Exception {

        mockMvc.perform(get("/interview/" + candidate.getId().toString() + "?interviewerId=" + interviewer.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(candidateDao, times(1)).getCandidateById(candidate.getId().toString());
        verify(interviewerDao, times(1)).getInterviewerById(interviewer.getId().toString());
        verify(interviewHourInterval, times(1)).calculateInterviews(any(), any());
    }

    @Test
    public void getInterviewWithMultipleInterviewers() throws Exception {

        mockMvc.perform(get("/interview/" + candidate.getId().toString() + "?interviewerId=" + interviewer.getId().toString() + "&interviewerId=" + interviewer.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(candidateDao, times(1)).getCandidateById(candidate.getId().toString());
        verify(interviewerDao, times(2)).getInterviewerById(interviewer.getId().toString());
        verify(interviewHourInterval, times(2)).calculateInterviews(any(), any());
    }
}
