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

    public AdminCourseDto(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.grade = course.getGrade();
    }
}
