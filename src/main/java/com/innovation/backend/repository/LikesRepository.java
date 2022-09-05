package com.innovation.backend.repository;

import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Likes;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByMemberAndInterview(Member member, Interview interview);
}
