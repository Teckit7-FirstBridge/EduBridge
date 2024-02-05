package com.ll.edubridge.domain.member.member.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;


@Getter
public class MyPageDto {


    private List<Course> learningCourses;

    private List<Course> favoriteCourses;

    private Long goalProgress;

    public MyPageDto() {
    }

    public MyPageDto(List<Course> learningCourses, List<Course> favoriteCourses, Long goalProgress) {
        this.learningCourses = learningCourses;
        this.favoriteCourses = favoriteCourses;
        this.goalProgress = goalProgress;
    }
}
