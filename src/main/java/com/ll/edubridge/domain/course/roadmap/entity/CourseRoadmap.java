package com.ll.edubridge.domain.course.roadmap.entity;

import com.ll.edubridge.domain.course.course.entity.Course;
import jakarta.persistence.ManyToOne;

public class CourseRoadmap {
    @ManyToOne
    private Course course;

    @ManyToOne
    private Roadmap roadmap;
}
