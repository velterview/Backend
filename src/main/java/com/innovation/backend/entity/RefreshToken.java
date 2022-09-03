package com.innovation.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String tokenValue;

    public RefreshToken(Member member, String tokenValue){
        this.member = member;
        this.tokenValue = tokenValue;
    }

    public void updateValue(String tokenValue){
        this.tokenValue = tokenValue;
    }

}
