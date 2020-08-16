package com.tamo.calendar.controller;

import com.tamo.calendar.dao.CandidateDao;
import com.tamo.calendar.model.client.Candidate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import org.testcontainers.shaded.javax.ws.rs.core.MediaType;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CandidateController.class)
public class CandidateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateDao candidateDao;

    @Test
    public void getCandidates() throws Exception {
        Candidate candidate = new Candidate("test","test@test.com");

        List<Candidate> candidates = List.of(candidate);
        given(candidateDao.getCandidatesList()).willReturn(candidates);

        mockMvc.perform(get("/candidates").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(candidate.getName()))
                .andExpect(jsonPath("$[0].email").value(candidate.getEmail()));

        verify(candidateDao).getCandidatesList();
    }

    @Test
    public void getCandidate() throws Exception {
        Candidate candidate = new Candidate("test", "test@email.com");

        given(candidateDao.getCandidateById("1")).willReturn(
                candidate
        );
        mockMvc.perform(get("/candidates/1"));
        verify(candidateDao).getCandidateById("1");
    }

    @Test
    public void returns200WhenCandidateIsValid() throws Exception {
        Candidate candidate = new Candidate("test", "test@email.com");

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(candidate);

        mockMvc.perform(post("/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }

    @Test
    public void returns400WhenNameEmpty() throws Exception {
        Candidate candidate = new Candidate("", "test@test.com");

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(candidate);

        mockMvc.perform(post("/candidates").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("name: Name cannot be empty"));
    }

    @Test
    public void returns400WhenEmailNotValid() throws Exception {
        Candidate candidate = new Candidate("test", "test");

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(candidate );

        mockMvc.perform(post("/candidates").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("email: Email must be valid"));
    }

    @Test
    public void returns400WhenEmailNull() throws Exception {
        Candidate candidate = new Candidate();
        candidate.setName("test");

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(candidate );

        mockMvc.perform(post("/candidates").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("email: Must specify an email"));
    }
}