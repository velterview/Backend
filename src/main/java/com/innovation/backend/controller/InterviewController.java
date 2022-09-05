package com.innovation.backend.controller;

import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class InterviewController {

    private final InterviewService interviewService;

    @RequestMapping(value = "/interview/{subtopicId}", method = RequestMethod.GET)
    public ResponseDto<?> getInterview(@PathVariable Long subtopicId) {
        return interviewService.getInterview(subtopicId);
    }

}
