package com.innovation.backend.service;

import com.innovation.backend.dto.request.LoginReqDto;
import com.innovation.backend.dto.request.SignupReqDto;
import com.innovation.backend.dto.response.MessageDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.dto.response.MemberInfoResDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.entity.RefreshToken;
import com.innovation.backend.exception.ErrorCode;
import com.innovation.backend.jwt.util.JwtUtil;
import com.innovation.backend.jwt.util.TokenProperties;
import com.innovation.backend.repository.MemberRepository;
import com.innovation.backend.repository.RefreshTokenRepository;
import com.innovation.backend.security.user.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //회원가입
    @Transactional
    public ResponseDto<?> signup(SignupReqDto signupReqDto) {
        String username = signupReqDto.getUsername();
        String nickname = signupReqDto.getNickname();
        String password = signupReqDto.getPassword();
        String passwordConfirm = signupReqDto.getPasswordConfirm();

        if (!idDuplicateCheck(username)) {return ResponseDto.fail(ErrorCode.DUPLICATE_ID);}

        else if(!nicknameDuplicateCheck(nickname)) {return ResponseDto.fail(ErrorCode.DUPLICATE_NICKNAME);}

        else if(!isSamePassword(password,passwordConfirm)) {return ResponseDto.fail(ErrorCode.PASSWORDS_NOT_MATCHED);}

        else {

            Member member = Member.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .build();

            memberRepository.save(member);

            MemberInfoResDto memberInfoResDto = MemberInfoResDto.builder()
                    .id(member.getId())
                    .username(username)
                    .nickname(nickname)
                    .build();
            return ResponseDto.success(memberInfoResDto);
        }

    }

    @Transactional
    public ResponseDto<?> login(LoginReqDto loginReqDto, HttpServletResponse response){
        String username = loginReqDto.getUsername();
        Member member = isPresentMemberByUsername(username);

        if(member == null){return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);}

        if(!member.validatePassword(passwordEncoder,loginReqDto.getPassword())){
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 토큰 발급
        String accessToken = jwtUtil.createToken(username,TokenProperties.AUTH_HEADER);
        String refreshToken = jwtUtil.createToken(username, TokenProperties.REFRESH_HEADER);

        RefreshToken refreshTokenFromDB = jwtUtil.getRefreshTokenFromDB(member);

        // 로그인 경력이 있는 사용자 -> DB에 Refresh Token 있음 -> 새로 로그인 했으면 새로 발급받는 토큰으로 변경
        // 로그인이 처음인 사용자 -> DB에 Refresh Token 없음 -> 발급받은 Refresh 토큰 저장
        if(refreshTokenFromDB == null){
            RefreshToken saveRefreshToken = RefreshToken.builder()
                    .member(member)
                    .tokenValue(refreshToken)
                    .build();

            refreshTokenRepository.save(saveRefreshToken);

        }else{
            refreshTokenFromDB.updateValue(refreshToken);
        }

        // 헤더에 응답으로 보내줌
        TokenToHeaders(response, accessToken, refreshToken);

        MemberInfoResDto memberInfoResDto = MemberInfoResDto.builder()
                .id(member.getId())
                .username(username)
                .nickname(member.getNickname())
                .build();
        return ResponseDto.success(memberInfoResDto);
    }

    @Transactional
    public ResponseDto<?> logout(HttpServletRequest request, UserDetailsImpl userDetails){

        Member member = userDetails.getMember();

        String refreshHeader = request.getHeader(TokenProperties.REFRESH_HEADER);

        if(refreshHeader == null){return ResponseDto.fail(ErrorCode.NEED_REFRESH_TOKEN);}

        if(!refreshHeader.startsWith(TokenProperties.TOKEN_TYPE)){
            return ResponseDto.fail(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String refreshToken = refreshHeader.replace(TokenProperties.TOKEN_TYPE,"");

        // 토큰 검증
        String refreshTokenValidate = jwtUtil.validateToken(refreshToken);


        switch (refreshTokenValidate) {
            case TokenProperties.VALID:
            case TokenProperties.EXPIRED:
                RefreshToken refreshTokenFromDB = jwtUtil.getRefreshTokenFromDB(member);
                if (refreshTokenFromDB != null && refreshToken.equals(refreshTokenFromDB.getTokenValue())) {
                    refreshTokenRepository.delete(refreshTokenFromDB);
                    MessageDto messageDto = MessageDto.builder()
                            .message("로그아웃 되었습니다.")
                            .build();
                    return ResponseDto.success(messageDto);
                } else {
                    return ResponseDto.fail(ErrorCode.INVALID_REFRESH_TOKEN);
                }
            default:
                return ResponseDto.fail(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    @Transactional
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshHeader = request.getHeader(TokenProperties.REFRESH_HEADER);
        String accessHeader = request.getHeader(TokenProperties.AUTH_HEADER);

        if (refreshHeader == null) {
            return ResponseDto.fail(ErrorCode.NEED_REFRESH_TOKEN);
        }

        if (!refreshHeader.startsWith(TokenProperties.TOKEN_TYPE)) {
            return ResponseDto.fail(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if(accessHeader == null){
            return ResponseDto.fail(ErrorCode.NEED_ACCESS_TOKEN);
        }

        if (!accessHeader.startsWith(TokenProperties.TOKEN_TYPE)) {
            return ResponseDto.fail(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        String refreshToken = refreshHeader.replace(TokenProperties.TOKEN_TYPE, "");
        String accessToken = accessHeader.replace(TokenProperties.TOKEN_TYPE, "");

        // Access 토큰 검증
        String AccessTokenValidate = jwtUtil.validateToken(accessToken);

        if (AccessTokenValidate.equals(TokenProperties.INVALID)) {
            return ResponseDto.fail(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        // Refresh 토큰 검증
        String refreshTokenValidate = jwtUtil.validateToken(refreshToken);

        switch (refreshTokenValidate) {
            case TokenProperties.EXPIRED:
                return ResponseDto.fail(ErrorCode.EXPIRED_REFRESH_TOKEN);
            case TokenProperties.VALID:
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                Member member = isPresentMemberByUsername(username);

                if (member == null) {
                    return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);
                } else {
                    RefreshToken refreshTokenFromDB = jwtUtil.getRefreshTokenFromDB(member);
                    if (refreshTokenFromDB != null && refreshToken.equals(refreshTokenFromDB.getTokenValue())) {
                        String newAccessToken = jwtUtil.createToken(username, TokenProperties.AUTH_HEADER);
                        response.addHeader(TokenProperties.AUTH_HEADER, TokenProperties.TOKEN_TYPE + newAccessToken);
                        MessageDto messageDto = MessageDto.builder()
                                .message("Access Token이 발급되었습니다.")
                                .build();
                        return ResponseDto.success(messageDto);
                    } else {
                        return ResponseDto.fail(ErrorCode.INVALID_REFRESH_TOKEN);
                    }
                }
            default:
                return ResponseDto.fail(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }


    private boolean idDuplicateCheck(String username){
        Member member = isPresentMemberByUsername(username);

        return member == null;
    }

    private boolean nicknameDuplicateCheck(String nickname){
        Member member = isPresentMemberByNickname(nickname);

        return member == null;
    }

    private  boolean isSamePassword(String password, String ConfirmPassword){
        return password.equals(ConfirmPassword);
    }

    //// 회원가입 조건 추가 ?
//    public boolean nullCheck(String valueForSignup){
//        return valueForSignup == null || valueForSignup.equals("") || valueForSignup.equals(" ");
//    }
//
//    private boolean idStrCheck (String username){
//        return Pattern.matches("^[a-zA-Z0-9]{4,12}$", username);
//    }
//
//    private boolean passwordStrCheck(String password){
//        return Pattern.matches("^[a-z0-9]{4,32}$", password);
//    }

    @Transactional(readOnly = true)
    public Member isPresentMemberByUsername(String username) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        return optionalMember.orElse(null);
    }

    @Transactional(readOnly = true)
    public Member isPresentMemberByNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    private void TokenToHeaders(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addHeader(TokenProperties.AUTH_HEADER, TokenProperties.TOKEN_TYPE + accessToken);
        response.addHeader(TokenProperties.REFRESH_HEADER, TokenProperties.TOKEN_TYPE + refreshToken);
    }

}
