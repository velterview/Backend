package com.innovation.backend.service;

import com.innovation.backend.dto.response.MyPageResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.*;
import com.innovation.backend.mockobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.util.AssertionErrors.assertNull;

class MyPageServiceTest {
    private Long memberId;
    private Long interviewid;
    private MockMemberRepository memberRepository;
    private MockMypageService mypageService;
    private MockInterviewRepository interviewRepository;
    private MockLikesRepository likesRepository;
    private MockAnswerRepository answerRepository;

    private Interview interview;
    private Answer answer;
    @BeforeEach
    void setUp() {
        memberId = 1L;
        interviewid = 1L;
        memberRepository = new MockMemberRepository();
        Member member = Member.builder().username("hyemin")
                .password("password")
                .nickname("hyemin")
                .build();
        memberRepository.save(member);

        interviewRepository = new MockInterviewRepository();
        likesRepository = new MockLikesRepository();
        answerRepository = new MockAnswerRepository();
        interview = Interview.builder()
                .question("스프링이란 무엇인가요?")
                .answer("스프링은 자바 기반의 웹 어플리케이션을 만들 수 있는 프레임워크입니다.")
                .subTopic(SubTopic.builder().topic(Topic.builder().name("backend").build()).name("spring").build())
                .reference("https://spring.io/projects/spring-framework")
                .build();
        interviewRepository.save(interview);

        answer =Answer.builder()
                .member(member)
                .interview(interview)
                .content("사실 스프링은 여러가지 기술들을 아우르는 말이다.\n" +
                        "우리가 흔히 스프링~스프링 하는 것은 사실 스프링의 여러가지 프로젝트들 중 스프링 프레임워크(Spring Framework)를 말하는 것이다.")
                .publicTF(true)
                .build();
        answerRepository.save(answer);


        mypageService = new MockMypageService(interviewRepository, likesRepository, answerRepository);
    }

    @Nested
    @DisplayName("내가 푼/ 찜함 문제 조회")
    class testReadMyPage {

        @DisplayName("문제 리스트 조회")
        @Test
        void readMypage() {
            ResponseDto<?> responseDto;
            Member member = memberRepository.findById(memberId);
            responseDto = mypageService.readMypage(member);
            MyPageResponseDto myPageResponseDto = (MyPageResponseDto) responseDto.getData();

            assertNotNull(memberId + "에 해당하는 사용자가 없습니다.", member);
            assertTrue("내가 푼/찜한 문제 조회에 실패하였습니다.", responseDto.isSuccess());

            for(MyPageResponseDto.MyInterview myInterview : myPageResponseDto.getInterview()){
                assertEquals(interview.getQuestion(), myInterview.getQuestion());
                assertEquals(interview.getAnswer(), myInterview.getAnswer());
            }

            for(MyPageResponseDto.MyInterview mylike : myPageResponseDto.getLike()){
                assertEquals(interview.getQuestion(), mylike.getQuestion());
                assertEquals(interview.getAnswer(), mylike.getAnswer());
                assertEquals(interview.getSubTopic().getName(), mylike.getSubtopic());
            }

            assertNull("에러 내용이 존재합니다", responseDto.getError());
        }

    }

    @Test
    void makePublic() {
        ResponseDto<?> responseDto;
        Member member = memberRepository.findById(memberId);

        responseDto = mypageService.makePublic(interviewid, member);

        assertNotNull(memberId + "에 해당하는 사용자가 없습니다.", member);
        assertTrue("내가 푼/찜한 문제 조회에 실패하였습니다.", responseDto.isSuccess());
        assertNull("에러 내용이 존재합니다", responseDto.getError());
    }
}