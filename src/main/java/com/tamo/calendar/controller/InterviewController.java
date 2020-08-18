package com.tamo.calendar.controller;

import com.tamo.calendar.dao.CandidateDao;
import com.tamo.calendar.dao.InterviewerDao;
import com.tamo.calendar.model.user.User;
import com.tamo.calendar.model.interview.Interview;
import com.tamo.calendar.utils.InterviewHourInterval;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/interview")
@Api(tags="Interview Controller")
public class InterviewController {
    @Autowired
    private CandidateDao candidateDao;

    @Autowired
    private InterviewerDao interviewerDao;

    @Autowired
    private InterviewHourInterval interviewHourInterval;

    @ApiOperation(value = "Get all possible interviews for the provided candidate and interviewers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful return all possible interviews"),
            @ApiResponse(code = 404, message = "Candidate and/or Interviewer/s do not exist"),
    })
    @GetMapping("/{candidateId}")
    public List<Interview> getInterview(
            @PathVariable String candidateId,
            @RequestParam(name="interviewerId") List<String> interIds)
    {
        User candidate = candidateDao.getCandidateById(candidateId);
        User interviewer = interviewerDao.getInterviewerById(interIds.get(0));
        List<Interview> interview = interviewHourInterval.calculateInterviews(
                candidate.returnDatesList(),
                interviewer.returnDatesList());
        for (int i = 1; i < interIds.size(); i++) {
            interviewer = interviewerDao.getInterviewerById(interIds.get(i));
            interview = interviewHourInterval.calculateInterviews(interview, interviewer.returnDatesList());
        }

        return interview;
    }
}
