package com.ll.edubridge.domain.home.admin.dto;


import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class AdminCourseEnrollDto {

    @NonNull
    private Long id;
    @NonNull
    private String name;

    public AdminCourseEnrollDto(CourseEnroll courseEnroll){
        this.id = courseEnroll.getId();
        this.name = courseEnroll.getMember().getNickname();
    }

}
