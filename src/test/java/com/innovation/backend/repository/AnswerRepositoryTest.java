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
    }
}