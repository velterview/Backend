package com.innovation.backend.repository;

import com.innovation.backend.entity.Likes;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    List<Likes> findAllByMember(Member member);
}
