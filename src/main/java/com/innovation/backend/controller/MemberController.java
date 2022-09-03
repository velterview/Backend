package com.innovation.backend.controller;

import com.innovation.backend.dto.request.member.LoginReqDto;
import com.innovation.backend.dto.request.member.SignupReqDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원가입 - 모두 접근 가능
    @PostMapping("/member/signup")
    public ResponseDto<?> signup(@RequestBody SignupReqDto SignupRequestDto) {
        return memberService.signup(SignupRequestDto);
    }

    // 로그인
    @PostMapping("/member/login")
    public ResponseDto<?> login(@RequestBody LoginReqDto loginReqDto, HttpServletResponse response) {
        return memberService.login(loginReqDto, response);
    }


}
