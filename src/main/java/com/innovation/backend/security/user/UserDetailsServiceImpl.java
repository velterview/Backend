package com.innovation.backend.security.user;

import com.innovation.backend.entity.Member;
import com.innovation.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElse(null);
        if (member == null) {
            return null;
        }else{
            UserDetailsImpl userDetails = new UserDetailsImpl();
            userDetails.setMember(member);
            return userDetails;
        }

    }
}
