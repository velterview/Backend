package com.innovation.backend.service;

import com.innovation.backend.dto.request.member.LoginReqDto;
import com.innovation.backend.dto.request.member.SignupReqDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.dto.response.member.MemberInfoResDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.entity.RefreshToken;
import com.innovation.backend.exception.ErrorCode;
import com.innovation.backend.jwt.util.JwtUtil;
import com.innovation.backend.jwt.util.TokenProperties;
import com.innovation.backend.repository.MemberRepository;
import com.innovation.backend.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

    private void setTokenOnHeader(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addHeader(TokenProperties.AUTH_HEADER, TokenProperties.TOKEN_TYPE + accessToken);
        response.addHeader(TokenProperties.REFRESH_HEADER, TokenProperties.TOKEN_TYPE + refreshToken);
    }

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
            signupReqDto.setPassword(passwordEncoder.encode(password));

            Member member = new Member(signupReqDto);

            memberRepository.save(member);

            MemberInfoResDto memberInfoResDto = MemberInfoResDto.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .build();
            return ResponseDto.success(memberInfoResDto);
        }

    }

    @Transactional
    public ResponseDto<?> login(LoginReqDto loginReqDto, HttpServletResponse response){
        Member member = memberRepository.findByUsername(loginReqDto.getUsername());

        if(member == null){return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);}

        if(!member.validatePassword(passwordEncoder,loginReqDto.getPassword())){
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 토큰 발급
        String accessToken = jwtUtil.createToken(member.getUsername(),TokenProperties.AUTH_HEADER);
        String refreshToken = jwtUtil.createToken(member.getUsername(), TokenProperties.REFRESH_HEADER);

        Optional<RefreshToken> dbRefreshToken = refreshTokenRepository.findByMember(member);

        // 로그인 경력이 있는 사용자 -> DB에 Refresh Token 있음 -> 새로 로그인 했으면 새로 발급받는 토큰으로 변경
        // 로그인이 처음인 사용자 -> DB에 Refresh Token 없음 -> 발급받은 Refresh 토큰 저장
        if(dbRefreshToken.isPresent()){
            dbRefreshToken.get().updateValue(refreshToken);
        }else{
            RefreshToken saveRefreshToken = new RefreshToken(member,refreshToken);
            refreshTokenRepository.save(saveRefreshToken);
        }

        // 헤더에 응답으로 보내줌
        setTokenOnHeader(response, accessToken, refreshToken);

        MemberInfoResDto memberInfoResDto = MemberInfoResDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
        return ResponseDto.success(memberInfoResDto);
    }


}
