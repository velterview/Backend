package com.innovation.backend.service;

import com.innovation.backend.dto.request.AnswerRequestDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.*;
import com.innovation.backend.exception.ErrorCode;
import com.innovation.backend.repository.AnswerRepository;
import com.innovation.backend.repository.InterviewRepository;
import com.innovation.backend.repository.SubTopicRepositoy;
import com.innovation.backend.repository.TopicRepository;
import com.innovation.backend.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final InterviewRepository interviewRepository;
    private final TopicRepository topicRepository;
    private final SubTopicRepositoy subTopicRepositoy;


    public void creatDummyData(){
        // 임시코드 - 임시 토픽 생성
        Topic topic = Topic.builder()
                .id(1L)
                .name("백엔드")
                .build();

        topicRepository.save(topic);


        // 임시코드 - 임시 부주제 생성
        SubTopic subTopic = SubTopic.builder()
                .id(1L)
                .name("자바")
                .topic(topic)
                .build();

        subTopicRepositoy.save(subTopic);

        // 임시코드 - 임시 인터뷰 생성
        Interview interview = Interview.builder()
                .id(1L)
                .subTopic(subTopic)
                .question("자바란 무엇인가요?")
                .answer("객체 지향 언어")
                .reference("ss")
                .build();

        interviewRepository.save(interview);
    }

    @Transactional
    public ResponseDto<?> createAnswer(Long interviewId, AnswerRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        creatDummyData();

        Member member = userDetails.getMember();

        Interview interview = isPresentInterview(interviewId);
        if (null == interview) {
            return ResponseDto.fail(ErrorCode.INTERVIEW_NOT_FOUND);
        }

        Answer answer = Answer.builder()
                .interview(interview)
                .member(member)
                .content(requestDto.getContent())
                .isPublic(requestDto.isPublic())
                .build();

        answerRepository.save(answer);

        return ResponseDto.success("답변 저장이 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    public Interview isPresentInterview(Long questionId) {
        Optional<Interview> interviewOptional = interviewRepository.findById(questionId);
        return interviewOptional.orElse(null);
    }
}
