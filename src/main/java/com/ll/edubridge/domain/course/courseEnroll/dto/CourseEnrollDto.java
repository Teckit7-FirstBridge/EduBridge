package com.ll.edubridge.domain.course.courseEnroll.dto;

import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import lombok.Getter;

@Getter
public class CourseEnrollDto {

    private Long courseId;

    private String title;

    private String imgUrl;



    public CourseEnrollDto(CourseEnroll courseEnroll){
        this.courseId=courseEnroll.getCourse().getId();
        this.title=courseEnroll.getCourse().getTitle();
        this.imgUrl=courseEnroll.getCourse().getImgUrl();
    }
}
