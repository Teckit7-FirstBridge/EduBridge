package com.ll.edubridge.domain.home.admin.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class RecentCourseDto {
    @NonNull
    private Long id;
    @NonNull
    private String title;

    public RecentCourseDto(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
    }
}
