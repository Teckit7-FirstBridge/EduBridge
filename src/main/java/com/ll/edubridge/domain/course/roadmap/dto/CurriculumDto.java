package com.ll.edubridge.domain.course.roadmap.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;

@Getter
public class CurriculumDto {
    private String title;
    private Long id;
    private int courseOrder;

    public CurriculumDto() {
    }
    public CurriculumDto(Course course,int courseOrder){
        this.title = course.getTitle();
        this.id = course.getId();
        this.courseOrder = courseOrder;
    }
}
