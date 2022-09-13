/*
package com.innovation.backend.mockobject.mockRepository;

import com.innovation.backend.entity.Answer;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public Optional<Answer> findByMemberAndInterview(Member member, Interview interview) {
        for (Answer answer : answers) {
            if (answer.getMember().equals(member) && answer.getInterview().equals(interview)) {
                return Optional.of(answer);
            }
        }
        return Optional.empty();
    }

    List<Answer> findAllByMember(Member member) {
        List<Answer> answerList = new ArrayList<>();
        for (Answer answer : answers) {
            if (answer.getMember().equals(member)) {
                answerList.add(answer);
            }
        }
        return answerList;
    }

    List<Answer> findAllByInterviewAndPublicTF(Interview interview, boolean tf) {
        List<Answer> answerList = new ArrayList<>();
        for (Answer answer : answers) {
            if (answer.getInterview().equals(interview) && Objects.equals(answer.isPublicTF(), tf)) {
                answerList.add(answer);
            }
        }
        return answerList;
    }
    Answer findByInterviewAndMember(Interview interview, Member member) {
        for (Answer answer : answers) {
            if (answer.getMember().equals(member) && answer.getInterview().equals(interview)) {
                return answer;
            }
        }
        return null;
    }


    public void deleteAll() {
        answers.clear();
    }
}
*/
