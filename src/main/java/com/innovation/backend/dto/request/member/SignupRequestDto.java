package com.innovation.backend.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String passwordConfirm;
    private String nickname;
}
