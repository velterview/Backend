package com.innovation.backend.dto.response;

import lombok.Builder;

@Builder
public class InterviewResponseDto {
    private Long id;
    private String question;
    private String answer;
    private String reference;
}
