package com.ll.edubridge.domain.home.admin.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class AdminCourseDto {
    @NonNull
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private int enrollCount;

    public AdminCourseDto(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.enrollCount = course.getCourseEnrollList().size();
    }
}
