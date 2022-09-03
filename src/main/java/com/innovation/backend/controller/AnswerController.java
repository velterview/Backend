package com.innovation.backend.controller;

import com.innovation.backend.dto.request.AnswerRequestDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class AnswerController {

    private final AnswerService answerService;

    @RequestMapping(value = "/auth/interview/{interviewId}", method = RequestMethod.POST)
    public ResponseDto<?> createAnswer(@PathVariable Long interviewId, HttpServletRequest request, @RequestBody AnswerRequestDto requestDto) {
        return answerService.createAnswer(interviewId, request, requestDto);
    }

    @RequestMapping(value = "/auth/interview/{interviewId}", method = RequestMethod.PUT)
    public ResponseDto<?> updateAnswer(@PathVariable Long interviewId, HttpServletRequest request, @RequestBody AnswerRequestDto requestDto) {
        return answerService.updateAnswer(interviewId, request, requestDto);
    }

    @RequestMapping(value = "/interview/{interviewId}/answers", method = RequestMethod.GET)
    public ResponseDto<?> getAnswers(@PathVariable Long interviewId) {
        return answerService.getAnswers(interviewId);
    }

}
