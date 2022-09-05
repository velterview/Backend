package com.innovation.backend.repository;

import com.innovation.backend.entity.Answer;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
    List<Answer> findAllByInterview(Interview interview);
//    List<Answer> findAllByInterviewAndPublicTrue(Interview interview);
    Answer findByInterviewAndMember(Interview interview, Member member);
}
