package com.ll.edubridge.domain.course.course.dto;


import lombok.Getter;

@Getter
public class CourseAuthDto {

    private boolean isEnroll;

    public CourseAuthDto(Boolean isEnroll){
        this.isEnroll=isEnroll;
    }

    public CourseAuthDto(){

    }

}
