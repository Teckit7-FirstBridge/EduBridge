package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class TitleIdCourseDto {
    private String title;
    private Long id;

    public TitleIdCourseDto(Course course){
        this.id = course.getId();
        this.title = course.getTitle();
    }
}
