package com.innovation.backend.repository;

import com.innovation.backend.entity.SubTopic;
import com.innovation.backend.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubTopicRepository extends JpaRepository<SubTopic,Long> {
    SubTopic findByName(String name);
    List<SubTopic> findAllByTopic(Topic topic);
}
