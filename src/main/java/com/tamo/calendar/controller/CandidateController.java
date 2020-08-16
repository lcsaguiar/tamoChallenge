package com.tamo.calendar.controller;

import com.tamo.calendar.dao.CandidateDao;
import com.tamo.calendar.dao.ClientDao;
import com.tamo.calendar.model.client.Candidate;
import com.tamo.calendar.model.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateDao dao;

    @PostMapping()
    public Candidate save(@Valid @RequestBody Candidate candidate) {
        dao.saveCandidate(candidate);
        return candidate;
    }

    @GetMapping()
    public List<Candidate> getCandidates() {
        return dao.getCandidatesList();
    }

    @GetMapping("/{id}")
    public Client getCandidate(@PathVariable String id) {
        return dao.getCandidateById(id);
    }
}
