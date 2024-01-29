package com.ll.edubridge.domain.course.courseEnroll.dto;

import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateCourseEnrollDto {

    private LocalDateTime cancelDate;

    public CreateCourseEnrollDto(CourseEnroll courseEnroll){
        this.cancelDate = courseEnroll.getCancelDate();

    }
}
