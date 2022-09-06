package com.innovation.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopicInterviewResponseDto {
    private Long subtopicId;
    private String subtopicName;
    private List<InterviewResponseDto> interviews;
}
