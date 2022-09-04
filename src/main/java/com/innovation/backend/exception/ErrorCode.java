package com.innovation.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //회원가입, 로그인 관련 에러
    DUPLICATE_ID("DUPLICATE_ID", "중복된 아이디가 있습니다."),
    DUPLICATE_NICKNAME("DUPLICATE_NICKNAME", "중복된 닉네임이 있습니다."),
    PASSWORDS_NOT_MATCHED("PASSWORDS_NOT_MATCHED", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    
    // 권한 요청 시 Access 토큰을 보내지 않은 경우
    INVALID_LOGIN("INVALID_LOGIN", "로그인이 필요합니다."),

    // reissue시 Refresh 토큰을 보내지 않은 경우
    NEED_REFRESH_TOKEN("NEED_REFRESH_TOKEN","Refresh Token이 필요합니다."),

    // 유효하지 않은 토큰
    INVALID_ACCESS_TOKEN("INVALID_ACCESS_TOKEN", "유효하지 않은 Access Token 입니다."),
    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", "유효하지 않은 Refresh Token 입니다."),
    
    // 만료된 토큰
    EXPIRED_ACCESS_TOKEN("EXPIRED_ACCESS_TOKEN", "만료된 Access Token 입니다."),
    EXPIRED_REFRESH_TOKEN("EXPIRED_REFRESH_TOKEN", "만료된 Refresh Token 입니다."),

    // 인터뷰 관련 에러
    INTERVIEW_NOT_FOUND("INTERVIEW_NOT_FOUND", "해당 인터뷰 id를 찾을 수 없습니다."),
    SUBTOPIC_NOT_FOUND("INTERVIEW_NOT_FOUND", "해당 인터뷰 id를 찾을 수 없습니다.");

    private final String code;
    private final String message;
}
