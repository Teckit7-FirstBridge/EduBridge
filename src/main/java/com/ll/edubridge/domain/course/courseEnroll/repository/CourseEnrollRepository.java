package com.ll.edubridge.domain.course.courseEnroll.repository;

import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseEnrollRepository extends JpaRepository<CourseEnroll, Long> {
    Optional<CourseEnroll> findById(Long id);
}
