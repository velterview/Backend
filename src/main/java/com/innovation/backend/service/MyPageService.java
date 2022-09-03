package com.innovation.backend.service;

import com.innovation.backend.dto.response.MessageResponseDto;
import com.innovation.backend.dto.response.MyPageResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Answer;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Likes;
import com.innovation.backend.entity.Member;
import com.innovation.backend.exception.ErrorCode;
import com.innovation.backend.repository.AnswerRepository;
import com.innovation.backend.repository.InterviewRepository;
import com.innovation.backend.repository.LikesRepository;
import com.innovation.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final MemberRepository memberRepository;
    private final InterviewRepository interviewRepository;
    private final LikesRepository likesRepository;
    private final AnswerRepository answerRepository;
    @Transactional
    public ResponseDto<?> readMypage(Member member){
        List<Interview> interviewList= interviewRepository.findAllByMember(member);
        List<Likes> likesList = likesRepository.findAllByMember(member);

        List<MyPageResponseDto.MyInterview> myInterviewList = new ArrayList<>();
        List<MyPageResponseDto.MyInterview> myLikeList = new ArrayList<>();
        for(Interview interview: interviewList)
        {
            MyPageResponseDto.MyInterview myInterview = getMyInterview(member, interview);
            myInterviewList.add(myInterview);

        }
        for(Likes likes : likesList){
            Interview interview = likes.getInterview();
            MyPageResponseDto.MyInterview myInterview = getMyInterview(member,interview);
            myLikeList.add(myInterview);
        }
        return ResponseDto.success(new MyPageResponseDto(myInterviewList,myLikeList));
    }

    public MyPageResponseDto.MyInterview getMyInterview(Member member, Interview interview){
        Optional<Answer> answerOptional = answerRepository.findByMemberAndInterview(member,interview);
        String myanswer = null;
        if(!answerOptional.isEmpty()){
            Answer answer = answerOptional.get();
            myanswer = answer.getContent();
        }

        return MyPageResponseDto.MyInterview.builder()
                .id(interview.getId())
                .topic(interview.getSubTopic().getTopic().getName())
                .subtopic(interview.getSubTopic().getName())
                .question(interview.getQuestion())
                .answer(interview.getAnswer())
                .myanswer(myanswer)
                .reference(interview.getReference()).build();
    }

}
