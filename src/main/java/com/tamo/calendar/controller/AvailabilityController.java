package com.tamo.calendar.controller;

import com.tamo.calendar.dao.AvailabilityDao;
import com.tamo.calendar.dao.UserDao;
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
@RequestMapping("/availabilities/users")
@Api(tags = "Availability Controller")
public class AvailabilityController {
   @Autowired
    private AvailabilityDao dao;

    @Autowired
    private UserDao userDao;

    @ApiOperation(value = "Get all availabilities")
    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns all users availabilities")
    })
    public List<Availability> Availabilities() {
        return dao.getAvailabilityList();
    }

    @ApiOperation(value = "Create an availability")
    @PostMapping("/{userId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Post availability in db successfully"),
            @ApiResponse(code = 400, message = "Availability does not have the required params"),
            @ApiResponse(code = 404, message = "UserId provided does not exist"),
    })
    public Availability save(@PathVariable String userId, @Valid @RequestBody() Availability availability) {
        availability.setUser(userDao.getUserById(userId));
        if(availability.getStart().isAfter(availability.getEnd())) {
            throw new DateNotValidException("Start date must be before than the end date");
        }
        dao.saveAvailability(availability);

        return availability;
    }
}
