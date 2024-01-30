package com.ll.edubridge.domain.course.courseEnroll.dto;

import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import lombok.Getter;

@Getter
public class CourseEnrollDto {

    private Long id;


    public CourseEnrollDto(CourseEnroll courseEnroll){
        this.id = courseEnroll.getId();


    }
}
