package com.innovation.backend.service;

import com.innovation.backend.dto.request.member.LoginRequestDto;
import com.innovation.backend.dto.request.member.SignupRequestDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.dto.response.member.SignupResponseDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.exception.ErrorCode;
import com.innovation.backend.repository.MemberRepository;
import lombok.AllArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;

    private boolean idDuplicateCheck(String username){
        Member member = memberRepository.findByUsername(username);

        return member == null;
    }

    private boolean nicknameDuplicateCheck(String nickname){
        Member member = memberRepository.findByNickname(nickname);

        return member == null;
    }

    private  boolean isSamePassword(String password, String ConfirmPassword){
        return password.equals(ConfirmPassword);
    }

    //회원가입
    @Transactional
    public ResponseDto<?> signup(SignupRequestDto SignupRequestDto) {
        String username = SignupRequestDto.getUsername();
        String nickname = SignupRequestDto.getNickname();
        String password = SignupRequestDto.getPassword();
        String passwordConfirm = SignupRequestDto.getPasswordConfirm();

        if (!idDuplicateCheck(username)) {return ResponseDto.fail(ErrorCode.DUPLICATE_ID);}

        else if(!nicknameDuplicateCheck(nickname)) {return ResponseDto.fail(ErrorCode.DUPLICATE_NICKNAME);}

        else if(!isSamePassword(password,passwordConfirm)) {return ResponseDto.fail(ErrorCode.PASSWORDS_NOT_MATCHED);}

        else {
            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setUsername(username);
//            loginRequestDto.setPassword(passwordEncoder.encode(password));
            loginRequestDto.setPassword(password);
            loginRequestDto.setNickname(nickname);

            Member member = new Member(loginRequestDto);

            memberRepository.save(member);

            SignupResponseDto signupResponseDto = SignupResponseDto.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .build();
            return ResponseDto.success(signupResponseDto);
        }

    }

}
