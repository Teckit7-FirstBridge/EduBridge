package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.roadmap.entity.CourseRoadmap;
import lombok.Getter;

@Getter
public class NumDto {

    private int num;

    public NumDto(CourseRoadmap courseRoadmap){
        this.num = courseRoadmap.getCourseOrder();
    }

    public NumDto(){
    }
}
