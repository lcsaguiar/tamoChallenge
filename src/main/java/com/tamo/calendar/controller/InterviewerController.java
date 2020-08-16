package com.tamo.calendar.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tamo.calendar.dao.InterviewerDao;
import com.tamo.calendar.model.client.Interviewer;
import javax.validation.Valid;

@RestController
@RequestMapping("/interviewers")
public class InterviewerController {
    @Autowired
    private InterviewerDao dao;

    @PostMapping()
    public Interviewer save(@Valid @RequestBody Interviewer interviewer) {
        dao.saveInterviewer(interviewer);
        return interviewer;
    }

    @GetMapping()
    public List<Interviewer> getInterviewers() {
        return dao.getInterviewersList();
    }

    @GetMapping("/{id}")
    public Interviewer getInterviewer(@PathVariable String id) {
        return dao.getInterviewerById(id);
    }
}
