package com.tamo.calendar.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tamo.calendar.dao.InterviewerDao;
import com.tamo.calendar.model.user.Interviewer;
import javax.validation.Valid;

@RestController
@RequestMapping("/interviewers")
@Api(tags="Interviewer Controller")
public class InterviewerController {
    @Autowired
    private InterviewerDao dao;

    @ApiOperation(value = "Creates an interviewer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully create the interviewer "),
            @ApiResponse(code = 400, message = "Request does not have a valid body"),
    })
    @PostMapping()
    public Interviewer save(@Valid @RequestBody Interviewer interviewer) {
        dao.saveInterviewer(interviewer);
        return interviewer;
    }

    @ApiOperation(value="Get all interviewers")
    @GetMapping()
    public List<Interviewer> getInterviewers() {
        return dao.getInterviewersList();
    }

    @ApiOperation(value="Get the specified interviewer")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful return the interviewer with the specified id"),
            @ApiResponse(code = 404, message = "Interviewer provided does not exist"),
    })
    public Interviewer getInterviewer(@PathVariable String id) {
        return dao.getInterviewerById(id);
    }
}
