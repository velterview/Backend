package com.innovation.backend.dto.request;

import com.innovation.backend.entity.Answer;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequestDto {
    private String content;
    private boolean publicTF;

    // 테스트용 메소드 : toEntity
    public Answer toEntity(Interview interview1, Member member1) {
        return Answer.builder().interview(interview1).member(member1).content(content).publicTF(publicTF).build();
    }
}
