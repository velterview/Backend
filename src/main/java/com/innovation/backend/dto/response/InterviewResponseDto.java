package com.innovation.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResponseDto {
    private Long id;
    private String question;
    private String answer;
    private String reference;
}
