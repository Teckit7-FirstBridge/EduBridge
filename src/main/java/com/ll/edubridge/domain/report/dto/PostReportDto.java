package com.ll.edubridge.domain.report.dto;

import com.ll.edubridge.domain.report.entity.PostReport;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostReportDto {

    @NotNull
    private long id; // 신고테이블에서 자동으로 생성된 신고번호

    @NotNull
    private LocalDateTime createDate;

    @NotNull
    private String content;

    @NotNull
    private long authorId; // 신고 하는 아이디

    @NotNull
    private long postId; // 신고 당한 글

    public PostReportDto(PostReport postReport){
        this.id = postReport.getId();
        this.createDate = postReport.getCreateDate();
        this.content = postReport.getContent();
        this.authorId = postReport.getWriter().getId();
        // getAuthorId() 안된 이유 : entity의 PostReport의 authourId 가 없기 때문에
        this.postId = postReport.getPostId();

    }



}
