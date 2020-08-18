package com.tamo.calendar.controller;

import com.tamo.calendar.CalendarApplication;
import com.tamo.calendar.dao.CandidateDao;
import com.tamo.calendar.dao.PostgresSqlContainer;
import com.tamo.calendar.model.user.Candidate;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import javax.transaction.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment=SpringBootTest.WebEnvironment.MOCK,
        classes = CalendarApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CandidateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresSqlContainer.getInstance();

    @Autowired
    private CandidateDao candidateDao;

    @Test
    public void getCandidates() throws Exception {
        Candidate candidate = new Candidate("test","test@test.com");
        candidateDao.saveCandidate(candidate);

        mockMvc.perform(get("/candidates").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(candidate.getName()))
                .andExpect(jsonPath("$[0].email").value(candidate.getEmail()));
    }
    @Test
    public void getCandidate() throws Exception {
        Candidate candidate = new Candidate("test", "test@email.com");
        candidateDao.saveCandidate(candidate);
        String id = candidate.getId().toString();

        mockMvc.perform(get("/candidates/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(candidate.getName()))
                .andExpect(jsonPath("$.email").value(candidate.getEmail()));
    }

    @Test
    public void getCandidateWithInvalidId() throws Exception {
        String id = "1";

        mockMvc.perform(get("/candidates/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").value("Candidate with id " + id + " not found"));
    }

    @Test
    public void postValidCandidate() throws Exception {
        Candidate candidate = new Candidate("test", "test@email.com");
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(candidate);

        mockMvc.perform(post("/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }
}
