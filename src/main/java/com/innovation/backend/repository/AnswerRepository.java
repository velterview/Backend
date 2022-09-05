package com.innovation.backend.repository;

import com.innovation.backend.entity.Answer;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
    List<Answer> findAllByInterview(Interview interview);
    Answer findByInterviewAndMember(Interview interview, Member member);
}
