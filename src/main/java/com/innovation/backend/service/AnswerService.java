package com.innovation.backend.service;

import com.innovation.backend.dto.request.AnswerRequestDto;
import com.innovation.backend.dto.response.AnswerResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Answer;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Member;
import com.innovation.backend.repository.AnswerRepository;
import com.innovation.backend.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.innovation.backend.exception.ErrorCode.INTERVIEW_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final InterviewRepository interviewRepository;

    // 임시 코드 - 임시 멤버 생성
    Member member = Member.builder()
            .id(1L)
            .username("test")
            .nickname("테스트 닉네임")
            .password("1234")
            .build();


    @Transactional
    public ResponseDto<?> createAnswer(Long interviewId, HttpServletRequest request, AnswerRequestDto requestDto) {

        Interview interview = isPresentInterview(interviewId);
        if (null == interview) {
            return ResponseDto.fail(INTERVIEW_NOT_FOUND);
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

    @Transactional
    public ResponseDto<?> getAnswers(Long interviewId) {
        Interview interview = isPresentInterview(interviewId);
        if (null == interview) {
            return ResponseDto.fail(INTERVIEW_NOT_FOUND);
        }

        List<Answer> answerList = answerRepository.findAllByInterviewAndPublicTrue(interview);
        List<AnswerResponseDto> answerResponseDtoList = new ArrayList<>();

        for (Answer answer : answerList) {
            answerResponseDtoList.add(
                    AnswerResponseDto.builder()
                            .nickname(answer.getMember().getNickname())
                            .content(answer.getContent())
                            .build()
            );
        }

        return ResponseDto.success(answerResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updateAnswer(Long interviewId, HttpServletRequest request, AnswerRequestDto requestDto) {
        Interview interview = isPresentInterview(interviewId);
        if (null == interview) {
            return ResponseDto.fail(INTERVIEW_NOT_FOUND);
        }

        Answer answer = answerRepository.findByInterviewAndMember(interview, member);

        answer.update(requestDto);
        return ResponseDto.success("답변 수정이 완료되었습니다.");


    }

    /*
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
    */

    @Transactional(readOnly = true)
    public Interview isPresentInterview(Long questionId) {
        Optional<Interview> interviewOptional = interviewRepository.findById(questionId);
        return interviewOptional.orElse(null);
    }

}
