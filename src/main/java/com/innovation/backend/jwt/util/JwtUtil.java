package com.innovation.backend.jwt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.exception.ErrorCode;
import com.innovation.backend.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;

    @Value("${jwt.secret.key}")
    private String secretKey;
    Key key;
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    // 토큰 생성
    public String createToken(String username, String type){
        Date date = new Date();
        int time = type.equals(TokenProperties.AUTH_HEADER)? TokenProperties.ACCESS_TOKEN_VALID_TIME : TokenProperties.REFRESH_TOKEN_VALID_TIME;

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(date)
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(key,signatureAlgorithm)
                .compact();
    }
    
    // 토큰 검증
    public boolean validateToken(HttpServletResponse response, String token, String type) throws IOException {

        ErrorCode expiredErrorCode = type.equals(TokenProperties.AUTH_HEADER) ? ErrorCode.EXPIRED_ACCESS_TOKEN : ErrorCode.EXPIRED_REFRESH_TOKEN;
        ErrorCode invalidErrorCode = type.equals(TokenProperties.AUTH_HEADER) ? ErrorCode.INVALID_ACCESS_TOKEN : ErrorCode.INVALID_REFRESH_TOKEN;

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            exceptionResponse(response, expiredErrorCode);
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException | UnsupportedJwtException | NullPointerException e) {
            exceptionResponse(response, invalidErrorCode);
        }
        return false;
    }

    // 예외 응답
    public void exceptionResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ResponseDto<?> responseDto = ResponseDto.fail(errorCode);
        String httpResponse = objectMapper.writeValueAsString(responseDto);
        response.getWriter().write(httpResponse);
    }

    // token에서 username 가져오기
    public String getUsernameFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

}
