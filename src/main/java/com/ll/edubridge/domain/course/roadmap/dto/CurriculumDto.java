package com.ll.edubridge.domain.course.roadmap.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CurriculumDto {
    private String title;
    private Long id;
    private int courseOrder;

    public CurriculumDto(Course course,int courseOrder){
        this.title = course.getTitle();
        this.id = course.getId();
        this.courseOrder = courseOrder;
    }
}
