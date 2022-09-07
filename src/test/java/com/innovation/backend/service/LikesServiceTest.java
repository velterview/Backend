package com.innovation.backend.service;

import com.innovation.backend.dto.response.LikesResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Interview;
import com.innovation.backend.entity.Member;
import com.innovation.backend.exception.ErrorCode;
import com.innovation.backend.mockobject.MockInterviewRepository;
import com.innovation.backend.mockobject.MockLikesRepository;
import com.innovation.backend.mockobject.MockLikesService;
import com.innovation.backend.mockobject.MockMemberRepository;
import org.junit.jupiter.api.*;

import static org.springframework.test.util.AssertionErrors.*;
@Nested
@DisplayName("문제 찜하기 테스트")
class LikesServiceTest {
    private Long memberId;
    private Long interviewid;
    private MockMemberRepository memberRepository;
    private MockInterviewRepository interviewRepository;
    private MockLikesRepository likesRepository;

    private MockLikesService likesService;

    @BeforeEach
    void setUp() {
        memberRepository = new MockMemberRepository();
        interviewRepository = new MockInterviewRepository();
        likesRepository = new MockLikesRepository();

        interviewRepository.save(Interview.builder()
                        .question("스프링이란 무엇인가요?")
                        .answer("스프링은 자바 기반의 웹 어플리케이션을 만들 수 있는 프레임워크입니다.")
                        .reference("https://spring.io/projects/spring-framework")
                .build());

        likesService = new MockLikesService(interviewRepository, likesRepository);
        interviewid = Long.valueOf(1);
        memberId = Long.valueOf(1);

        memberRepository.save(Member.builder().username("hyemin")
                .password("password")
                .nickname("hyemin")
                .build());
    }

    @AfterEach
    void tearDown() {
    }
    @DisplayName("문제 찜하기")
    @Test
    void createLike() {
        ResponseDto<?> responseDto;

        Member member = memberRepository.findById(memberId);

        responseDto = likesService.createLike(interviewid, member);
        LikesResponseDto data = (LikesResponseDto) responseDto.getData();

        assertNotNull(memberId + "에 해당하는 사용자가 없습니다.", member);
        assertTrue("찜하기에 실패하였습니다.", responseDto.isSuccess());
        assertEquals("데이터 메세지가 다릅니다", "질문을 찜하였습니다.",data.getMessage());
        assertNull("에러 내용이 존재합니다",responseDto.getError());

    }

    @DisplayName("문제 찜하기 중복")
    @Test
    void createLike2() {
        createLike();
        ResponseDto<?> responseDto;

        Member member = memberRepository.findById(memberId);

        responseDto = likesService.createLike(interviewid, member);

        assertNotNull(memberId + "에 해당하는 사용자가 없습니다.", member);
        assertFalse("중복 체크에 실패하였습니다", responseDto.isSuccess());
        assertEquals("에러내용에 다릅니다", ErrorCode.DUPLICATE_LIKES,responseDto.getError());

    }
    @DisplayName("문제 찜하기 취소")
    @Test
    void deleteLike() {
        createLike();
        ResponseDto<?> responseDto;

        Member member = memberRepository.findById(memberId);

        responseDto = likesService.deleteLike(interviewid, member);
        LikesResponseDto data = (LikesResponseDto) responseDto.getData();
        assertNotNull(memberId + "에 해당하는 사용자가 없습니다.", member);
        assertTrue("찜하기 취소에 실패하였습니다.", responseDto.isSuccess());
        assertEquals("데이터 메세지가 다릅니다", "찜한 질문을 취소하였습니다.",data.getMessage());
        assertNull("에러 내용이 존재합니다",responseDto.getError());
    }

    @DisplayName("문제 찜하기 취소 중복")
    @Test
    void deleteLike2() {
        ResponseDto<?> responseDto;

        Member member = memberRepository.findById(memberId);

        responseDto = likesService.deleteLike(interviewid, member);

        assertNotNull(memberId + "에 해당하는 사용자가 없습니다.", member);
        assertFalse("찜하기 취소에 실패하였습니다.", responseDto.isSuccess());
        assertEquals("에러 내용이 다릅니다",ErrorCode.LIKES_NOT_FOUND,responseDto.getError());
    }

    @DisplayName("문제 찜하기/취소")
    @Test
    void selectedLike(){
        ResponseDto<?> responseDto;

        Member member = memberRepository.findById(memberId);

        responseDto = likesService.selectedLike(interviewid, member);
        LikesResponseDto data = (LikesResponseDto) responseDto.getData();

        assertNotNull(memberId + "에 해당하는 사용자가 없습니다.", member);
        assertTrue("찜하기에 실패하였습니다.", responseDto.isSuccess());
        assertEquals("데이터 메세지가 다릅니다", "질문을 찜하였습니다.",data.getMessage());
        assertNull("에러 내용이 존재합니다",responseDto.getError());

        responseDto = likesService.selectedLike(interviewid, member);
        data = (LikesResponseDto) responseDto.getData();

        assertNotNull(memberId + "에 해당하는 사용자가 없습니다.", member);
        assertTrue("찜하기 취소에 실패하였습니다.", responseDto.isSuccess());
        assertEquals("데이터 메세지가 다릅니다", "찜한 질문을 취소하였습니다.",data.getMessage());
        assertNull("에러 내용이 존재합니다",responseDto.getError());


    }

}