package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;

@Getter
public class NumDto {

    private int num;

    public NumDto(Course course){
        this.num = course.getRoadmapNum();

    }

    public NumDto(){

    }
}
