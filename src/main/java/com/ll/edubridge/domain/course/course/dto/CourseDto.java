package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CourseDto {

    private Long id;
    private String title;
    private String notice;
    private String imgUrl;
    private String overView;
    private LocalDateTime createDate;

    public CourseDto(Course course){
        this.id = course.getId();
        this.title = course.getTitle();
        this.notice = course.getNotice();
        this.imgUrl = course.getImgUrl();
        this.overView = course.getOverView();
        this.createDate = course.getCreateDate();
    }
}