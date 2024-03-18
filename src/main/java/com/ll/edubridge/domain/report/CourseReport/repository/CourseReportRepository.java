package com.ll.edubridge.domain.report.CourseReport.repository;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.domain.report.CourseReport.entity.CourseReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseReportRepository extends JpaRepository<CourseReport, Long> {
    Optional<CourseReport> findByCourseId(Long courseId);

    Page<CourseReport> findBypostId(Member member, Pageable pageable);
}
