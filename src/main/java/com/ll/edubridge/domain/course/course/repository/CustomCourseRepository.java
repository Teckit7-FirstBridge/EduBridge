package com.ll.edubridge.domain.course.course.repository;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.standard.base.KwTypeCourse;
import com.ll.edubridge.standard.base.KwTypeV1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCourseRepository {

    Page<Course> findByKwAdmin(KwTypeCourse kwType, String kw, Member author, Pageable pageable);

    Page<Course> findByKw(KwTypeCourse kwType, String kw, Member author, Pageable pageable);
    List<Course> findLatestCourse(int num);
    List<Course> findByVoter(Member member);

    Page<Course> findByWriterId(Member author, Pageable pageable);
}
