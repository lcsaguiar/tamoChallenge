package com.tamo.calendar.controller;

import com.tamo.calendar.dao.InterviewerDao;
import com.tamo.calendar.model.client.Interviewer;
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
@WebMvcTest(InterviewerController.class)
public class InterviewerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterviewerDao interviewerDao;

    @Test
    public void getInterviewers() throws Exception {
        Interviewer interviewer = new Interviewer("test", "test@test.com");

        List<Interviewer> interviewers = List.of(interviewer);
        given(interviewerDao.getInterviewersList()).willReturn(interviewers);

        mockMvc.perform(get("/interviewers").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(interviewer.getName()))
                .andExpect(jsonPath("$[0].email").value(interviewer.getEmail()));

        verify(interviewerDao).getInterviewersList();
    }

    @Test
    public void getInterviewer() throws Exception {
        Interviewer interviewer = new Interviewer("test", "test@email.com");

        given(interviewerDao.getInterviewerById("1")).willReturn(
                interviewer
        );

        mockMvc.perform(get("/interviewers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@email.com"));

        verify(interviewerDao).getInterviewerById("1");
    }

    @Test
    public void returns200WhenInterviewerIsValid() throws Exception {
        Interviewer interviewer = new Interviewer("test", "test@email.com");

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(interviewer);

        mockMvc.perform(post("/interviewers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }

    @Test
    public void returns400WhenNameEmpty() throws Exception {
        Interviewer interviewer = new Interviewer("", "test@test.com");

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(interviewer);

        mockMvc.perform(post("/interviewers").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("name: Name cannot be empty"));
    }

    @Test
    public void returns400WhenEmailNotValid() throws Exception {
        Interviewer interviewer = new Interviewer("test", "test");

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(interviewer );

        mockMvc.perform(post("/interviewers").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("email: Email must be valid"));
    }

    @Test
    public void returns400WhenEmailNull() throws Exception {
        Interviewer interviewer = new Interviewer();
        interviewer.setName("test");

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(interviewer );

        mockMvc.perform(post("/interviewers").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("email: Must specify an email"));
    }
}
