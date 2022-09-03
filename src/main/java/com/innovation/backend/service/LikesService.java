package com.innovation.backend.service;

import com.innovation.backend.dto.response.LikesResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Likes;
import com.innovation.backend.entity.Member;
import com.innovation.backend.exception.ErrorCode;
import com.innovation.backend.repository.InterviewRepository;
import com.innovation.backend.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class LikesService {
    private final LikesRepository likesRepository;
    private final InterviewRepository interviewRepository;

    @Transactional
    public ResponseDto<?> createLike(Long interviewId, Member member) {
        Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
        if (interviewOptional.isEmpty()) return ResponseDto.fail(ErrorCode.INTERVIEW_NOT_FOUND);

        Interview interview = interviewOptional.get();
        Optional<Likes> likesOptional = likesRepository.findByMemberAndInterview(member, interview);
        if (likesOptional.isEmpty()) {
            Likes likes = Likes.builder()
                    .member(member)
                    .interview(interview)
                    .build();
            likesRepository.save(likes);
            return ResponseDto.success(LikesResponseDto.builder().message("질문을 찜하였습니다."));
        }else{
            return ResponseDto.fail(ErrorCode.DUPLICATE_LIKES);
        }
    }

    @Transactional
    public ResponseDto<?> deleteLike(Long interviewId, Member member) {
        Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
        if (interviewOptional.isEmpty()) return ResponseDto.fail(ErrorCode.INTERVIEW_NOT_FOUND);

        Interview interview = interviewOptional.get();
        Optional<Likes> likesOptional = likesRepository.findByMemberAndInterview(member, interview);
        if (likesOptional.isEmpty()) {
            return ResponseDto.fail(ErrorCode.LIKES_NOT_FOUND);
        }else{
            Likes likes = likesOptional.get();
            likesRepository.delete(likes);
            return ResponseDto.success(LikesResponseDto.builder().message("찜한 질문을 취소하였습니다."));
        }
    }
}
