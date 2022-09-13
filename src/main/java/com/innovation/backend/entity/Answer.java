package com.innovation.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innovation.backend.dto.request.AnswerRequestDto;
import lombok.*;

import javax.persistence.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean publicTF = true;

    public void update(AnswerRequestDto answerRequestDto) {
        this.content = answerRequestDto.getContent();
        this.publicTF = answerRequestDto.isPublicTF();
    }
    public void makePublic(){
        this.publicTF = !this.publicTF;
    }
}
