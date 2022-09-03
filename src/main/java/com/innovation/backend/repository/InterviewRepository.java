package com.innovation.backend.repository;

import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.SubTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview,Long> {
    List<Interview> findAllBySubTopic(SubTopic subTopic);
}
