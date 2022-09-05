package com.innovation.backend.repository;

import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview,Long> {

}
