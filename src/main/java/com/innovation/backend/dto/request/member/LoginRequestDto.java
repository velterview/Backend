package com.innovation.backend.dto.request.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
    private String nickname;
}
