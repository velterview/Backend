package com.innovation.backend.mockobject;

import com.innovation.backend.dto.response.LikesResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Likes;
import com.innovation.backend.entity.Member;
import com.innovation.backend.exception.ErrorCode;

import java.util.Optional;

public class MockLikesService {
    private final MockInterviewRepository interviewRepository;
    private final MockLikesRepository likesRepository;

    public MockLikesService(){
        interviewRepository = new MockInterviewRepository();
        likesRepository = new MockLikesRepository();
    }
    public MockLikesService(MockInterviewRepository interviewRepository,MockLikesRepository likesRepository){
        this.interviewRepository = interviewRepository;
        this.likesRepository = likesRepository;
    }

    public ResponseDto<?> createLike(Long interviewid, Member member) {
        Optional<Interview> interviewOptional = interviewRepository.findById(interviewid);
        if (interviewOptional.isEmpty()) return ResponseDto.fail(ErrorCode.INTERVIEW_NOT_FOUND);

        Interview interview = interviewOptional.get();
        Optional<Likes> likesOptional = likesRepository.findByMemberAndInterview(member, interview);
        if (likesOptional.isEmpty()) {
            Likes likes = Likes.builder()
                    .member(member)
                    .interview(interview)
                    .build();
            likesRepository.save(likes);
            return ResponseDto.success(LikesResponseDto.builder().message("질문을 찜하였습니다.").build());
        }else{
            return ResponseDto.fail(ErrorCode.DUPLICATE_LIKES);
        }
    }

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
            return ResponseDto.success(LikesResponseDto.builder().message("찜한 질문을 취소하였습니다.").build());
        }
    }
}
