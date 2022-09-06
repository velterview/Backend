package com.innovation.backend.controller;

import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
public class InterviewController {

    private final InterviewService interviewService;

    @RequestMapping(value = "/interview", method = RequestMethod.GET)
    public ResponseDto<?> getInterviewByTopic(@RequestParam(value="topic", defaultValue = "frontend") String topicName) {
        return interviewService.getInterviewByTopic(topicName);
    }

    @RequestMapping(value = "/interview/start", method = RequestMethod.GET)
    public ResponseDto<?> getInterviewBySubtopic(@RequestParam(value="subtopic", defaultValue = "react") String subtopicName) {
        return interviewService.getInterviewBySubtopic(subtopicName);
    }

}
