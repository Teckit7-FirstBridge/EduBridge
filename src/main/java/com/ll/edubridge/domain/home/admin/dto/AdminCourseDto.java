package com.ll.edubridge.domain.home.admin.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class AdminCourseDto {
    @NonNull
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String grade;
    @NonNull
    private int enrollCount;

    public AdminCourseDto(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.enrollCount = course.getCourseEnrollList().size();
    }
}
