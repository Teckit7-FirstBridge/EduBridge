package com.ll.edubridge.domain.report.CourseReport.dto;

import com.ll.edubridge.domain.report.CourseReport.entity.CourseReport;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class CreateCourseReportDto {
    @NonNull
    private String content;
    @NonNull
    private Long authorId;
    @NonNull
    private Long courseId;


    public CreateCourseReportDto(CourseReport courseReport) {
        this.content = courseReport.getContent();
        this.authorId = courseReport.getWriter().getId();
        this.courseId = courseReport.getCourseId();
    }
}
