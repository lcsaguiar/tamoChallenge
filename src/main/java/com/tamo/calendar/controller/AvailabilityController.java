package com.tamo.calendar.controller;

import com.tamo.calendar.dao.AvailabilityDao;
import com.tamo.calendar.dao.ClientDao;
import com.tamo.calendar.exceptions.DateNotValidException;
import com.tamo.calendar.model.interview.Availability;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/availabilities/clients")
@Api(tags = "Availability Controller")
public class AvailabilityController {
   @Autowired
    private AvailabilityDao dao;

    @Autowired
    private ClientDao clientDao;

    @ApiOperation(value = "Get all availabilities")
    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns all clients availabilities")
    })
    public List<Availability> Availabilities() {
        return dao.getAvailabilityList();
    }

    @ApiOperation(value = "Create an availability")
    @PostMapping("/{clientId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post availability in db successfully"),
            @ApiResponse(code = 400, message = "Availability does not have the required params"),
            @ApiResponse(code = 404, message = "ClientId provided does not exist"),
    })
    public Availability save(@PathVariable String clientId, @Valid @RequestBody() Availability availability) {
        availability.setClient(clientDao.getClientById(clientId));
        if(availability.getStart_duration().isAfter(availability.getEnd_duration())) {
            throw new DateNotValidException("Start date must be before than the end date");
        }
        dao.saveAvailability(availability);
        return availability;
    }
}
