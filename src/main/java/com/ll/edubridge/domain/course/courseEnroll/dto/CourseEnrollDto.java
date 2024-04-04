package com.ll.edubridge.domain.course.courseEnroll.dto;

import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CourseEnrollDto {

    private Long courseId;

    private String title;

    private String imgUrl;

    private int price;

    public CourseEnrollDto(CourseEnroll courseEnroll){
        this.courseId=courseEnroll.getCourse().getId();
        this.title=courseEnroll.getCourse().getTitle();
        this.imgUrl=courseEnroll.getCourse().getImgUrl();
        this.price=courseEnroll.getCourse().getPrice();
    }
}
