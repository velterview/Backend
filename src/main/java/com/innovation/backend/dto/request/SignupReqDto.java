package com.innovation.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupReqDto {
    private String username;
    private String password;
    private String passwordConfirm;
    private String nickname;
}
