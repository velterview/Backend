package com.innovation.backend.service;

import com.innovation.backend.dto.response.InterviewResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.dto.response.TopicInterviewResponseDto;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.SubTopic;
import com.innovation.backend.entity.Topic;
import com.innovation.backend.repository.InterviewRepository;
import com.innovation.backend.repository.SubTopicRepository;
import com.innovation.backend.repository.TopicRepository;
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
    private final TopicRepository topicRepository;
    private final SubTopicRepository subTopicRepository;

    @Transactional
    public ResponseDto<?> getInterviewByTopic(String topicName) {
        Topic topic = isPresentTopic(topicName);
        if (null == topic) {
            return ResponseDto.fail(SUBTOPIC_NOT_FOUND);
        }

        List<SubTopic> subTopicList = subTopicRepository.findAllByTopic(topic);
        List<TopicInterviewResponseDto> topicInterviewResponseDtoList = new ArrayList<>();

        for(SubTopic subTopic : subTopicList) {
            topicInterviewResponseDtoList.add(
                    TopicInterviewResponseDto.builder()
                            .subtopicId(subTopic.getId())
                            .subtopicName(subTopic.getName())
                            .interviews(interviewListExtractor(subTopic))
                            .build()
            );
        }

        return ResponseDto.success(topicInterviewResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> getInterviewBySubtopic(String subtopicName) {
        SubTopic subTopic = isPresentSubtopic(subtopicName);
        if (null == subTopic) {
            return ResponseDto.fail(SUBTOPIC_NOT_FOUND);
        }
        return ResponseDto.success(interviewListExtractor(subTopic));
    }

    @Transactional
    public List<InterviewResponseDto> interviewListExtractor(SubTopic subTopic) {
        List<Interview> interviewList = interviewRepository.findAllBySubTopic(subTopic);
        List<InterviewResponseDto> interviewResponseDtoList = new ArrayList<>();

        for (Interview interview : interviewList) {
            interviewResponseDtoList.add(
                    InterviewResponseDto.builder()
                            .id(interview.getId())
                            .question(interview.getQuestion())
                            .answer(interview.getAnswer())
                            .reference(interview.getReference())
                            .build()
            );
        }
        return interviewResponseDtoList;
    }

    @Transactional(readOnly = true)
    public SubTopic isPresentSubtopic(String subtopicName) {
        Optional<SubTopic> subTopicOptional = Optional.ofNullable(subTopicRepository.findByName(subtopicName));
        return subTopicOptional.orElse(null);
    }

    @Transactional(readOnly = true)
    public Topic isPresentTopic(String topicName) {
        Optional<Topic> topicOptional = Optional.ofNullable(topicRepository.findByName(topicName));
        return topicOptional.orElse(null);
    }

}
