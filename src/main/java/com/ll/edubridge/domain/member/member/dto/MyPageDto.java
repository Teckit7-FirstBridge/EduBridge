package com.ll.edubridge.domain.member.member.dto;

import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;


@Getter
public class MyPageDto {


    private List<CourseDto> learningCourses;

    private List<CourseDto> favoriteCourses;

    private Long goalProgress;

    public MyPageDto() {
    }

    public MyPageDto(List<CourseDto> learningCourses, List<CourseDto> favoriteCourses, Long goalProgress) {
        this.learningCourses = learningCourses;
        this.favoriteCourses = favoriteCourses;
        this.goalProgress = goalProgress;
    }

}
