package com.innovation.backend.controller;

import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/interview")
    public ResponseDto<?> getTopics() {
        return topicService.getTopics();
    }
}
