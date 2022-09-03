package com.innovation.backend.jwt.util;

public interface TokenProperties {
    String AUTH_HEADER = "Authorization";
    String REFRESH_HEADER = "Refresh-Token";
    String TOKEN_TYPE = "BEARER ";
    String Issuer = "ky";

    // Access JWT 토큰의 유효기간: 30분 (단위: milliseconds)
//    int ACCESS_JWT_TOKEN_VALID_TIME = 30 * 60 * 1000;
    int ACCESS_TOKEN_VALID_TIME = 2 * 60 * 1000;

    // Refresh JWT 토큰의 유효기간: 하루 (단위: milliseconds)
    int REFRESH_TOKEN_VALID_TIME = 24 * 60 * 60 * 1000;
}
