package com.ll.edubridge.domain.member.member.dto;

import com.ll.edubridge.domain.course.course.dto.CourseDto;
import lombok.Getter;

import java.util.List;


@Getter
public class MyPageDto {


    private List<CourseDto> learningCourses;

    private List<CourseDto> favoriteCourses;

    private MemberDto member;

    public MyPageDto(List<CourseDto> learningCourses, List<CourseDto> favoriteCourses, MemberDto member) {
        this.learningCourses = learningCourses;
        this.favoriteCourses = favoriteCourses;
        this.member = member;

    }
    public MyPageDto() {
    }
}
