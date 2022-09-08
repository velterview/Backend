package com.innovation.backend.repository;

import com.innovation.backend.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("signup - save member")
    void saveMember(){

        Member member = Member.builder()
                .username("usertest1")
                .password("12345")
                .nickname("coconut")
                .build();

        Member saveMember = memberRepository.save(member);

        Assertions.assertThat(saveMember).as("저장된 회원이 다릅니다.")
                .isSameAs(member); // actual, expected
        Assertions.assertThat(saveMember.getUsername()).as("회원의 아이디(이름)가 잘못 저장되었습니다.")
                .isEqualTo(member.getUsername());
        Assertions.assertThat(saveMember.getNickname()).as("회원의 닉네임이 잘못 저장되었습니다.")
                .isEqualTo(member.getNickname());
        Assertions.assertThat(saveMember.getPassword()).as("회원의 비밀번호가 잘못 저장되었습니다.")
                .isEqualTo(member.getPassword());
    }

    @Test
    @DisplayName("find member by username")
    void findMemberByUsername(){

        Member member = Member.builder()
                .username("usertest1")
                .password("12345")
                .nickname("coconut")
                .build();

        Member saveMember = memberRepository.save(member);


        Member findMember = memberRepository.findByUsername(saveMember.getUsername()).orElse(null);

        Assertions.assertThat(findMember).as("회원의 아이디(이름)로 잘못된 회원을 찾았습니다.").
                isSameAs(member);
        Assertions.assertThat(findMember).as("회원의 아이디로 회원을 찾을 수 없습니다.")
                .isNotNull();
        Assertions.assertThat(findMember.getUsername()).as("찾은 회원의 아이디와 기대되는 아이디가 다릅니다.")
                .isEqualTo(member.getUsername());
        Assertions.assertThat(findMember.getNickname()).as("찾은 회원의 닉네임과 기대되는 닉네임이 다릅니다.")
                .isEqualTo(member.getNickname());
        Assertions.assertThat(findMember.getPassword()).as("찾은 회원의 비밀번호와 기대되는 비밀번호가 다릅니다.")
                .isEqualTo(member.getPassword());

    }

    @Test
    @DisplayName("find member by nickname")
    void findMemberByNickname(){

        Member member = Member.builder()
                .username("usertest2")
                .password("123456")
                .nickname("papaya")
                .build();

        Member saveMember = memberRepository.save(member);


        Member findMember = memberRepository.findByNickname(saveMember.getNickname()).orElse(null);

        Assertions.assertThat(findMember).as("회원의 닉네임으로 잘못된 회원을 찾았습니다.")
                .isSameAs(member);
        Assertions.assertThat(findMember).as("회원의 닉네임으로 회원을 찾을 수 없습니다.")
                .isNotNull();
        Assertions.assertThat(findMember.getUsername()).as("찾은 회원의 아이디와 기대되는 아이디가 다릅니다.")
                .isEqualTo(member.getUsername());
        Assertions.assertThat(findMember.getNickname()).as("찾은 회원의 닉네임과 기대되는 닉네임이 다릅니다.")
                .isEqualTo(member.getNickname());
        Assertions.assertThat(findMember.getPassword()).as("찾은 회원의 비밀번호와 기대되는 비밀번호가 다릅니다.")
                .isEqualTo(member.getPassword());

    }

}
