package com.innovation.backend.repository;

import com.innovation.backend.entity.*;
import com.innovation.backend.mockobject.MockInterviewRepository;
import com.innovation.backend.mockobject.MockMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

//@WebAppConfiguration
//@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    Member mockMember = Member.builder()
            .username("cheolsu")
            .password("password")
            .nickname("김철수")
            .build();

    Interview mockInterview = Interview.builder()
            .question("스프링이란?")
            .answer("웹 어플리케이션을 만들 수 있는 자바 기반의 웹 프레임워크이다.")
            .subTopic(SubTopic.builder()
                    .topic(Topic.builder()
                            .name("BACKEND")
                            .build())
                    .name("SPRING")
                    .build())
            .build();

    @BeforeEach
    void setUp() {

        MockMemberRepository memberRepository = new MockMemberRepository();
        memberRepository.save(mockMember);

        MockInterviewRepository interviewRepository = new MockInterviewRepository();
        interviewRepository.save(mockInterview);
    }

    @Test
    @DisplayName("save answer")
    void saveAnswer() {

        // given
        Answer answer = Answer.builder()
                .interview(mockInterview)
                .member(mockMember)
                .content("스프링은 자바 기반 프레임워크입니다.")
                .publicTF(true)
                .build();

        // when
        Answer savedAnswer = answerRepository.save(answer);

        // then
        Assertions.assertThat(answer).isSameAs(savedAnswer);
        Assertions.assertThat(answer.getContent()).isEqualTo(savedAnswer.getContent());
        Assertions.assertThat(answer.isPublicTF()).isEqualTo(savedAnswer.isPublicTF());
    }

    @Test
    @DisplayName("find answer")
    void findAnswer() {

        // given
        Answer answer1 = Answer.builder()
                .interview(mockInterview)
                .member(mockMember)
                .content("공개 답변 내용 1")
                .publicTF(true)
                .build();

        Answer answer2 = Answer.builder()
                .interview(mockInterview)
                .member(mockMember)
                .content("비공개 답변 내용 2")
                .publicTF(false)
                .build();

        Answer savedAnswer1 = answerRepository.save(answer1);
        Answer savedAnswer2 = answerRepository.save(answer2);

        // when
        Answer findAnswer1 = answerRepository.findById(savedAnswer1.getId())
                .orElseThrow(() -> new IllegalArgumentException("Wrong AnswerId:<" + savedAnswer1.getId() + ">"));
        Answer findAnswer2 = answerRepository.findById(savedAnswer2.getId())
                .orElseThrow(() -> new IllegalArgumentException("Wrong AnswerId:<" + savedAnswer2.getId() + ">"));

        // then
        Assertions.assertThat(findAnswer1.getInterview()).isEqualTo(mockInterview);
        Assertions.assertThat(findAnswer1.getMember()).isEqualTo(mockMember);
        Assertions.assertThat(findAnswer1.getContent()).isEqualTo("공개 답변 내용 1");
        Assertions.assertThat(findAnswer1.isPublicTF()).isEqualTo(true);
        Assertions.assertThat(findAnswer2.getContent()).isEqualTo("비공개 답변 내용 2");
        Assertions.assertThat(findAnswer2.isPublicTF()).isEqualTo(false);


    }
}