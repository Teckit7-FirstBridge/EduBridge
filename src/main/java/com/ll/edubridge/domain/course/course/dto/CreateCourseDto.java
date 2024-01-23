package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;

@Getter
public class CreateCourseDto {
    private String title;
    private String notice;
    private String imgUrl;
    private String overView;

    public CreateCourseDto(Course course){
        this.title = course.getTitle();
        this.notice = course.getNotice();
        this.imgUrl = course.getImgUrl();
        this.overView = course.getOverView();
    }
}