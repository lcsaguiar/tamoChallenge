package com.tamo.calendar.controller;

import com.tamo.calendar.dao.CandidateDao;
import com.tamo.calendar.dao.InterviewerDao;
import com.tamo.calendar.model.client.Client;
import com.tamo.calendar.model.interview.Duration;
import com.tamo.calendar.utils.InterviewHourInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interview")
public class InterviewController {
    @Autowired
    private CandidateDao candidateDao;

    @Autowired
    private InterviewerDao interviewerDao;

    @Autowired
    private InterviewHourInterval interviewHourInterval;

    @GetMapping("/{candidateId}")
    public List<Duration> getInterview(@PathVariable String candidateId, @RequestParam(name="interviewerId") List<String> interIds) {
        Client candidate = candidateDao.getCandidateById(candidateId);
        Client interviewer = interviewerDao.getInterviewerById(interIds.get(0));
        List<Duration> duration = interviewHourInterval.calculateInterviews(candidate.returnDatesList(), interviewer.returnDatesList());
        for (int i = 1; i < interIds.size(); i++) {
            interviewer = interviewerDao.getInterviewerById(interIds.get(i));
            duration = interviewHourInterval.calculateInterviews(duration, interviewer.returnDatesList());
        }

        return duration;
    }
}
