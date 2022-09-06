package com.innovation.backend.repository;

import com.innovation.backend.entity.Answer;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
    Optional<Answer> findByMemberAndInterview(Member member, Interview interview);
    List<Answer> findAllByMember(Member member);
    List<Answer> findAllByInterviewAndPublicTF(Interview interview, boolean tf);
    Answer findByInterviewAndMember(Interview interview, Member member);
}
