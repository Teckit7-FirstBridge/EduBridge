package com.ll.edubridge.domain.course.course.repository;

import com.ll.edubridge.domain.course.course.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findById(Long id);

    Page<Course> findAll(Pageable pageable);
}