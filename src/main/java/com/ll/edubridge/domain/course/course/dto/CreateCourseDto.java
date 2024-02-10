package com.ll.edubridge.domain.course.course.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateCourseDto {

    @NotEmpty
    @Size(max=200)
    private String title;
    @NotEmpty
    @Size(max=1000)
    private String notice;
    @NotEmpty
    @Size(max=150)
    private String imgUrl;
    @NotEmpty
    @Size(max=500)
    private String overView;
    @NotEmpty
    private String grade;

    public CreateCourseDto(Course course) {
        this.title = course.getTitle();
        this.notice = course.getNotice();
        this.imgUrl = course.getImgUrl();
        this.overView = course.getOverView();
        this.grade = course.getGrade();
    }

    public CreateCourseDto() {

    }
}