package com.tamo.calendar.controller;

import com.tamo.calendar.dao.AvailabilityDao;
import com.tamo.calendar.dao.UserDao;
import com.tamo.calendar.model.user.User;
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
    private UserDao userDao;

    @Test
    public void getAvailabilitiesList() throws Exception {
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        Availability availability = new Availability(time, time, new User());
        List<Availability> availabilities = List.of(availability);
        given(availabilityDao.getAvailabilityList()).willReturn(availabilities);

        mockMvc.perform(get("/availabilities/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(availabilityDao).getAvailabilityList();
    }

    @Test
    public void saveAvailability() throws Exception {
        String dateString = "2020-09-16T08";
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("start", dateString);
        jsonRequest.put("end", dateString);
        String id = "1";

        mockMvc.perform(post("/availabilities/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value(dateString))
                .andExpect(jsonPath("$.end").value(dateString));

        verify(userDao).getUserById(id);
    }
}
