package com.tamo.calendar.controller;

import com.tamo.calendar.CalendarApplication;
import com.tamo.calendar.dao.AvailabilityDao;
import com.tamo.calendar.dao.CandidateDao;
import com.tamo.calendar.dao.PostgresSqlContainer;
import com.tamo.calendar.model.interview.Availability;
import com.tamo.calendar.model.user.Candidate;
import org.json.JSONObject;
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
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
public class AvailabilityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresSqlContainer.getInstance();

    @Autowired
    private AvailabilityDao availabilityDao;


    @Autowired
    private CandidateDao candidateDao;

    @Test
    public void getAvailabilitiesList() throws Exception {
        LocalDateTime time = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS);
        Candidate candidate = new Candidate("test", "test@test.com");
        candidateDao.saveCandidate(candidate);
        Availability availability = new Availability(time, time, candidate);
        availabilityDao.saveAvailability(availability);

        mockMvc.perform(get("/availabilities/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].start").value(time.toString()))
                .andExpect(jsonPath("$[0].end").value(time.toString()));
    }

    @Test
    public void postValidAvailability() throws Exception {
        Candidate candidate = new Candidate("test", "test@test.com");
        candidateDao.saveCandidate(candidate);
        String id = candidate.getId().toString();

        String dateString = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS).toString();
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("start", dateString);
        jsonRequest.put("end", dateString);

        mockMvc.perform(post("/availabilities/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.start").value(dateString))
                .andExpect(jsonPath("$.end").value(dateString));
    }

    @Test
    public void postAvailabilityWithInvalidUser() throws Exception {
        String dateString = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS).toString();
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("start", dateString);
        jsonRequest.put("end", dateString);
        String id = "1";

        mockMvc.perform(post("/availabilities/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").value("User with id "+ id + " not found"));
    }
}
