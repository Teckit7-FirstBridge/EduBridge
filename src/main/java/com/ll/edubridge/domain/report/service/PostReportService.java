package com.ll.edubridge.domain.report.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.report.dto.CreatePostReportDto;
import com.ll.edubridge.domain.report.entity.PostReport;
import com.ll.edubridge.domain.report.repository.PostReportRepository;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 데이터 변경이나 삭제 작업은 수행 불가능
public class PostReportService {
    private final PostReportRepository postReportRepository;
    private Rq rq;


    @Transactional // 데이터를 변경하는 작업에는 별도로 @Transactional 어노테이션 추가
    public PostReport create(Member member, CreatePostReportDto createPostReportDto, Long postId) {
        PostReport postReport = PostReport.builder()
                .content(createPostReportDto.getContent()) // 신고 내용
                .writer(member) // 신고자
                .postId(postId) // 신고 당한 글
                .build();

        return postReportRepository.save(postReport);
    }

    //  현재 사용자가 주어진 postId의 신고를 취소할 권한이 있는지 검사하는 로직
    public boolean haveAuthority(Long postId) {
        Member member = rq.getMember();

        PostReport postReport = this.getPostReport(postId);

        if (member == null) return false; // 현재 사용자가 null이 아니어야 한다.

        if (rq.isAdmin()) return true; // 현재 사용자가 관리자

        return postReport.getWriter().equals(member); // 신고글의 작성자와 현재 사용자가 동일해야 한다.
    }

    // 주어진 postId에 해당하는 PostReport 객체를 찾아 반환합니다. 객체를 찾지 못하면 GlobalException을 발생시켜 에러를 반환한다.
    public PostReport getPostReport(Long postId) {
        Optional<PostReport> postReport = this.findByPostId(postId);
        if (postReport.isPresent()) {
            return postReport.get();
        } else {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(), CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }

    // 주어진 postId에 해당하는 PostReport 객체를 찾아 Optional<PostReport>로 반환 , optional은
    // 해당 객체가 존재하지 않을 경우를 안전하게 처리하기 위해 사용
    public Optional<PostReport> findByPostId(Long postId) {
        return postReportRepository.findByPostId(postId);
    }

    //주어진 postId에 해당하는 신고를 삭제한다. 이는 먼저 getPostReport를 호출하여 해당 PostReport 객체를 얻고,
    // 그 객체를 postReportRepository.delete 메소드에 전달하여 삭제한다.
    public void delete(Long postId) {
        PostReport PostReport = this.getPostReport(postId);
        postReportRepository.delete(PostReport);

    }
}

