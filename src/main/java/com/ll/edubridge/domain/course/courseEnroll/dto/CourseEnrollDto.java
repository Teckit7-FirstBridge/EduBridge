package com.ll.edubridge.domain.course.courseEnroll.dto;

import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CourseEnrollDto {

    private Long id;
    private LocalDateTime cancelDate;

    public CourseEnrollDto(CourseEnroll courseEnroll){
        this.id = courseEnroll.getId();
        this.cancelDate = courseEnroll.getCancelDate();

    }
}
