package com.tamo.calendar.controller;

import com.tamo.calendar.CalendarApplication;
import com.tamo.calendar.dao.InterviewerDao;
import com.tamo.calendar.dao.PostgresSqlContainer;
import com.tamo.calendar.model.user.Interviewer;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment=SpringBootTest.WebEnvironment.MOCK,
        classes = CalendarApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class InterviewerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresSqlContainer.getInstance();

    @Autowired
    private InterviewerDao interviewerDao;

    @Test
    public void getInterviewers() throws Exception {
        Interviewer interviewer = new Interviewer("test","test@test.com");
        interviewerDao.saveInterviewer(interviewer);

        mockMvc.perform(get("/interviewers").contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(interviewer.getName()))
                .andExpect(jsonPath("$[0].email").value(interviewer.getEmail()));
    }
    @Test
    public void getInterviewer() throws Exception {
        Interviewer interviewer = new Interviewer("test", "test@email.com");
        interviewerDao.saveInterviewer(interviewer);
        String id = interviewer.getId().toString();

        mockMvc.perform(get("/interviewers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(interviewer.getName()))
                .andExpect(jsonPath("$.email").value(interviewer.getEmail()));
    }

    @Test
    public void getInterviewerWithInvalidId() throws Exception {
        String id = "1";

        mockMvc.perform(get("/interviewers/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").value("Interviewer with id " + id + " not found"));
    }

    @Test
    public void postWithValidInterviewer() throws Exception {
        Interviewer interviewer = new Interviewer("test", "test@email.com");
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(interviewer);

        mockMvc.perform(post("/interviewers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }
}
