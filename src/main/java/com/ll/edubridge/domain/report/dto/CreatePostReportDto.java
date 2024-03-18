package com.ll.edubridge.domain.report.dto;

import com.ll.edubridge.domain.report.entity.PostReport;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreatePostReportDto {

    @NotNull
    private String content;

    @NotNull
    private Long authorId;

    @NotNull
    private Long postId;




    public CreatePostReportDto(PostReport postReport) {
        this.content = postReport.getContent();
        this.authorId = postReport.getWriter().getId();
        this.postId = postReport.getPostId();
    }
}
