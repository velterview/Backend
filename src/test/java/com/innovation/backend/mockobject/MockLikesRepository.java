package com.innovation.backend.mockobject;

import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Likes;
import com.innovation.backend.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockLikesRepository {
    private List<Likes> likes = new ArrayList<>();

    private Long likeId = 1L;

    public Likes save(Likes like) {
        like.setId(likeId);
        ++likeId;
        likes.add(like);
        return like;
    }

    public Optional<Likes> findById(Long id) {
        for (Likes like : likes) {
            if (like.getId().equals(id)) {
                return Optional.of(like);
            }
        }

        return Optional.empty();
    }

    public List<Likes> findAll() {
        return likes;
    }

    public Optional<Likes> findByMemberAndInterview(Member member, Interview interview) {
        for (Likes like: likes){
            if(like.getMember().equals(member)&& like.getInterview().equals(interview)){
                return Optional.of(like);
            }
        }
        return Optional.empty();
    }

    public void delete(Likes like) {
       likes.remove(like);
    }

    public List<Likes> findAllByMember(Member member) {
        List<Likes> likelist = new ArrayList<>();
        for(Likes like :likes){
            if(like.getMember().equals(member)){
                likelist.add(like);
            }
        }
        return likelist;
    }
}
