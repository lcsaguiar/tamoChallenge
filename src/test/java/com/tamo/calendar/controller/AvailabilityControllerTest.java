package com.tamo.calendar.controller;

import com.tamo.calendar.controller.AvailabilityController;
import com.tamo.calendar.dao.AvailabilityDao;
import com.tamo.calendar.dao.ClientDao;
import com.tamo.calendar.model.client.Client;
import com.tamo.calendar.model.interview.Availability;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
@WebMvcTest(AvailabilityController.class)
public class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityDao availabilityDao;

    @MockBean
    private ClientDao clientDao;

    @Test
    public void getAvailabilitiesList() throws Exception {
        Availability availability = new Availability(
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                new Client());

        List<Availability> availabilities = List.of(availability);
        given(availabilityDao.getAvailabilityList()).willReturn(availabilities);

        mockMvc.perform(get("/availabilities/clients").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].start_duration").value(availability.getStart_duration().toString()))
                .andExpect(jsonPath("$[0].end_duration").value(availability.getEnd_duration().toString()));

        verify(availabilityDao).getAvailabilityList();
    }

    @Test
    public void saveAvailability() throws Exception {
        String dateString = "2020-09-16T08:00";
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("start_duration", dateString);
        jsonRequest.put("end_duration", dateString);
        String id = "1";

        mockMvc.perform(post("/availabilities/clients/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start_duration").value(dateString))
                .andExpect(jsonPath("$.end_duration").value(dateString));

        verify(clientDao).getClientById(id);
    }
}
