package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.course.entity.Course;

public class titleIdCourseDto {
    private String title;
    private Long id;

    public titleIdCourseDto() {
    }
    public titleIdCourseDto(Course course){
        this.id = course.getId();
        this.title = course.getTitle();
    }
}
