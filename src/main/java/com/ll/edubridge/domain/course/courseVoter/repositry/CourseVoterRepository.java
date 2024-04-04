package com.ll.edubridge.domain.course.courseVoter.repositry;

import com.ll.edubridge.domain.course.courseVoter.entity.CourseVoter;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseVoterRepository extends JpaRepository <CourseVoter,Long> {
    public List<CourseVoter> findByMember(Member member);
    public void deleteCourseVoterByCourseAndMember(Course course,Member member);
}
