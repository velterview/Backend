package com.innovation.backend.mockobject;

import com.innovation.backend.entity.Member;
import java.util.ArrayList;
import java.util.List;

public class MockMemberRepository {
    private List<Member> members = new ArrayList<>();

    private Long memberId = 1L;

    public Member save(Member member) {
        member.setId(memberId);
        ++memberId;
        members.add(member);
        return member;
    }

    public Member findById(Long id) {
        for (Member member : members) {
            if (member.getId().equals(id)) {
                return member;
            }
        }

        return null;
    }

    public Member findByUsername(String username) {
        for (Member member : members) {
            if (member.getUsername().equals(username)) {
                return member;
            }
        }

        return null;
    }

    public Member findByNickname(String nickname) {
        for (Member member : members) {
            if (member.getNickname().equals(nickname)) {
                return member;
            }
        }

        return null;
    }

    public List<Member> findAll() {
        return members;
    }

}
