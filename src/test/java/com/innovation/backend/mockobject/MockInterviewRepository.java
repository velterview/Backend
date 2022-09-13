package com.innovation.backend.mockobject;

import com.innovation.backend.entity.Interview;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockInterviewRepository {
    private List<Interview> interviews = new ArrayList<>();

    private Long interviewId = 1L;

    public Interview save(Interview interview) {
        interview.setId(interviewId);
        ++interviewId;
        interviews.add(interview);
        return interview;
    }

    public void mockSave(Interview interview) {
        interviews.add(interview);
    }

    public Optional<Interview> findById(Long id) {
        for (Interview interview : interviews) {
            if (interview.getId().equals(id)) {
                return Optional.of(interview);
            }
        }

        return Optional.empty();
    }

    public List<Interview> findAll() {
        return interviews;
    }


}
