package com.tamo.calendar.controller;

import com.tamo.calendar.dao.CandidateDao;
import com.tamo.calendar.model.client.Candidate;
import com.tamo.calendar.model.client.Client;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/candidates")
@Api(tags="Candidate Controller")
public class CandidateController {
    @Autowired
    private CandidateDao dao;

    @ApiOperation(value = "Creates a candidate")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully create the candidate "),
            @ApiResponse(code = 400, message = "Request does not have a valid body"),
    })
    @PostMapping()
    public Candidate save(@Valid @RequestBody Candidate candidate) {
        dao.saveCandidate(candidate);
        return candidate;
    }

    @ApiOperation(value = "Get all candidates")
    @GetMapping()
    public List<Candidate> getCandidates() {
        return dao.getCandidatesList();
    }

    @ApiOperation(value="Get the specified candidate")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful return the candidate with the specified id"),
            @ApiResponse(code = 404, message = "Candidate provided does not exist"),
    })
    @GetMapping("/{id}")
    public Client getCandidate(@PathVariable String id) {
        return dao.getCandidateById(id);
    }
}
