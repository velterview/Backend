package com.innovation.backend.service;

import com.innovation.backend.dto.request.AnswerRequestDto;
import com.innovation.backend.dto.response.AnswerResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Answer;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Member;
import com.innovation.backend.exception.ErrorCode;
import com.innovation.backend.repository.AnswerRepository;
import com.innovation.backend.repository.InterviewRepository;
import com.innovation.backend.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.innovation.backend.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final InterviewRepository interviewRepository;

    @Transactional
    public ResponseDto<?> createAnswer(Long interviewId, AnswerRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Interview interview = isPresentInterview(interviewId);
        if (null == interview) {
            return ResponseDto.fail(INTERVIEW_NOT_FOUND);
        }

        Member member = userDetails.getMember();

        boolean alreadyExist = answerAlreadyExist(interview, member);
        if (alreadyExist) {
            return ResponseDto.fail(DUPLICATE_ANSWER);
        }

        Answer answer = Answer.builder()
                .interview(interview)
                .member(member)
                .content(requestDto.getContent())
                .publicTF(requestDto.isPublicTF())
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

        List<Answer> answerList = answerRepository.findAllByInterviewAndPublicTF(interview, true);
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
    public ResponseDto<?> updateAnswer(Long interviewId, AnswerRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Interview interview = isPresentInterview(interviewId);
        if (null == interview) {
            return ResponseDto.fail(INTERVIEW_NOT_FOUND);
        }

        Member member = userDetails.getMember();
        Answer answer = answerRepository.findByInterviewAndMember(interview, member);

        answer.update(requestDto);
        return ResponseDto.success("답변 수정이 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    public Interview isPresentInterview(Long questionId) {
        Optional<Interview> interviewOptional = interviewRepository.findById(questionId);
        return interviewOptional.orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean answerAlreadyExist(Interview interview, Member member) {
        Answer already_exist = answerRepository.findByInterviewAndMember(interview, member);
        return already_exist != null;
    }
}
