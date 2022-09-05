package com.innovation.backend.controller;

import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.security.user.UserDetailsImpl;
import com.innovation.backend.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/interview")
public class LikesController {
    private final LikesService likesService;

    @RequestMapping(value = "/{interviewId}/like", method = RequestMethod.POST)
    public ResponseDto<?> createLike(@PathVariable Long interviewId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        return likesService.createLike(interviewId, member);
    }
    @RequestMapping(value = "/{interviewId}/like", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteLike(@PathVariable Long interviewId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        return likesService.deleteLike(interviewId, member);
    }
}
