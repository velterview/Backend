package com.innovation.backend.repository;

import com.innovation.backend.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview,Long> {
}
