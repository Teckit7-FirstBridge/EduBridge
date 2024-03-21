package com.ll.edubridge.domain.course.courseEnroll.repository;

import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import com.ll.edubridge.domain.member.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseEnrollRepository extends JpaRepository<CourseEnroll, Long> {
    Optional<CourseEnroll> findById(Long id);

    Page<CourseEnroll> findByMemberId(Pageable pageable, Long memberId);

    Optional<CourseEnroll> findByCourseIdAndMemberId(Long courseId, Long memberId);

    List<CourseEnroll> findByCourseId(Long courseId);

    List<CourseEnroll> findByMember(Member member);

}
