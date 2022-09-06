package com.innovation.backend.service;

import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.dto.response.TopicResponseDto;
import com.innovation.backend.entity.Topic;
import com.innovation.backend.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;

    @Transactional
    public ResponseDto<?> getTopics() {

        List<Topic> topicList = topicRepository.findAll();
        List<TopicResponseDto> topicResponseDtoList = new ArrayList<>();

        for (Topic topic : topicList) {
            topicResponseDtoList.add(
                    TopicResponseDto.builder()
                            .id(topic.getId())
                            .name(topic.getName())
                            .build()
            );
        }
        return ResponseDto.success(topicResponseDtoList);
    }
}
