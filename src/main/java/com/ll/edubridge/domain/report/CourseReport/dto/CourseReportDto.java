package com.ll.edubridge.domain.report.CourseReport.dto;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.report.CourseReport.entity.CourseReport;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
public class CourseReportDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private String content;
    @NonNull
    private long authorId;
    @NonNull
    private long courseId;

    public CourseReportDto(CourseReport courseReport, Member member) {
        this.id = courseReport.getId();
        this.createDate = courseReport.getCreateDate();
        this.content = courseReport.getContent();
        this.authorId = courseReport.getWriter().getId();
        this.courseId = courseReport.getCourseId();
    }
}
