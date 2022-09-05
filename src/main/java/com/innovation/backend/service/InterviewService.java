package com.innovation.backend.service;

import com.innovation.backend.dto.response.InterviewResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.SubTopic;
import com.innovation.backend.repository.InterviewRepository;
import com.innovation.backend.repository.SubTopicRepositoy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.innovation.backend.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final SubTopicRepositoy subTopicRepositoy;

    @Transactional
    public ResponseDto<?> getInterview(Long subtopicId) {
        SubTopic subTopic = isPresentSubtopic(subtopicId);
        if (null == subTopic) {
            return ResponseDto.fail(SUBTOPIC_NOT_FOUND);
        }

        List<Interview> interviewList = interviewRepository.findAllBySubTopic(subTopic);
        List<InterviewResponseDto> interviewResponseDtoList = new ArrayList<>();

        for (Interview interview : interviewList) {
            interviewResponseDtoList.add(
                    InterviewResponseDto.builder()
                            .id(interview.getId())
                            .question(interview.getQuestion())
                            .answer(interview.getAnswer())
                            .build()
            );
        }

        return ResponseDto.success(interviewResponseDtoList);
    }

    @Transactional(readOnly = true)
    public SubTopic isPresentSubtopic(Long subtopicId) {
        Optional<SubTopic> subTopicOptional = subTopicRepositoy.findById(subtopicId);
        return subTopicOptional.orElse(null);
    }

    @Transactional(readOnly = true)
    public Interview isPresentInterview(Long questionId) {
        Optional<Interview> interviewOptional = interviewRepository.findById(questionId);
        return interviewOptional.orElse(null);
    }

}
