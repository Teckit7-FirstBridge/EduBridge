package com.ll.edubridge.domain.course.roadmap.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.roadmap.entity.CourseRoadmap;
import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CreateCourseRoadmapDto {

    @NonNull
    private Course course;

    @NonNull
    private Roadmap roadmap;

    @NonNull
    private int courseOrder;

    @NonNull
    private Member RoadmapOwner;

    public CreateCourseRoadmapDto(CourseRoadmap courseRoadmap) {
        this.course = courseRoadmap.getCourse();
        this.roadmap = courseRoadmap.getRoadmap();
        this.courseOrder = courseRoadmap.getCourseOrder();
        this.RoadmapOwner = courseRoadmap.getRoadmap().getOwner();
    }
}
