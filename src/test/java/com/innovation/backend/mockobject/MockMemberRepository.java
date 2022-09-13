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

    public void mockSave(Member member) {
        members.add(member);
    }

    public Member findById(Long id) {
        for (Member member : members) {
            if (member.getId().equals(id)) {
                return member;
            }
        }

        return null;
    }

    public List<Member> findAll() {
        return members;
    }

    public void deleteAll() {

    }
}
