package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Likes;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageResponseDto {
    private List<MyInterview> interview;
    private List<MyInterview> like;

    @Builder
    public static class MyInterview {
        private Long id;
        private String topic;
        private String subtopic;
        private String question;
        private String answer;
        private String myanswer;
        private String reference;

    }
}
