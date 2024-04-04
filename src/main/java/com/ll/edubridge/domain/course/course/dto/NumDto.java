package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.roadmap.entity.CourseRoadmap;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class NumDto {

    private int num;

    public NumDto(CourseRoadmap courseRoadmap){
        this.num = courseRoadmap.getCourseOrder();
    }

}
