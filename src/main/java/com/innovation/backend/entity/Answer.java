package com.innovation.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isPublic;

    public void makePublic(){
        this.isPublic = !this.isPublic;
    }

}
