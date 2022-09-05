package com.innovation.backend.repository;

import com.innovation.backend.entity.Member;
import com.innovation.backend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByMember(Member member);
}
