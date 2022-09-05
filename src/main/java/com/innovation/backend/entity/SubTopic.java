package com.innovation.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "topic_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Topic topic;

    @Column(nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "subTopic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Interview> interviews;
}
