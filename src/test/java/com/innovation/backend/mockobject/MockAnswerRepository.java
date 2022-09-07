package com.innovation.backend.mockobject;

import com.innovation.backend.entity.Answer;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Answer;
import com.innovation.backend.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockAnswerRepository {

    private List<Answer> answers = new ArrayList<>();

    private Long answerId = 1L;

    public Answer save(Answer answer) {
        answer.setId(answerId);
        ++answerId;
        answers.add(answer);
        return answer;
    }

    public Optional<Answer> findById(Long id) {
        for (Answer answer : answers) {
            if (answer.getId().equals(id)) {
                return Optional.of(answer);
            }
        }

        return Optional.empty();
    }

    public List<Answer> findAll() {
        return answers;
    }
    public List<Answer> findAllByMember(Member member) {
        List<Answer> results = new ArrayList<>();
        for(Answer answer : answers){
            if(answer.getMember().equals(member)){
                results.add(answer);
            }
        }
        return results;
    }

    public Optional<Answer> findByMemberAndInterview(Member member, Interview interview) {
        for(Answer answer : answers){
            if(answer.getMember().equals(member)&&answer.getInterview().equals(interview)){
                return Optional.of(answer);
            }
        }
        return Optional.empty();
    }
}
