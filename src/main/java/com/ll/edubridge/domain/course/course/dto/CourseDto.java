package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;

@Getter
public class CourseDto {

    private Long id;
    private String title;
    private String notice;
    private String imgUrl;
    private String overView;
    private int price;

    public CourseDto(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.notice = course.getNotice();
        this.imgUrl = course.getImgUrl();
        this.overView = course.getOverView();
        this.price = course.getPrice();
    }

    public CourseDto() {

    }

}