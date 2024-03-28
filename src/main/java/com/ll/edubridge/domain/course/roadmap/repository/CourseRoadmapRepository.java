package com.ll.edubridge.domain.course.roadmap.repository;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.roadmap.entity.CourseRoadmap;
import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRoadmapRepository extends JpaRepository<CourseRoadmap, Long> {
    List<CourseRoadmap> findByCourse(Course course);

    boolean existsByCourseAndRoadmap(Course course, Roadmap roadmap);

    CourseRoadmap findByCourseAndRoadmap(Course course, Roadmap roadmap);

    CourseRoadmap findCourseRoadmapById(Long id);
}
