package com.ll.edubridge.domain.course.courseVoter.service;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.courseVoter.entity.CourseVoter;
import com.ll.edubridge.domain.course.courseVoter.repositry.CourseVoterRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseVoterService {
    private final CourseVoterRepository courseVoterRepository;

    @Transactional
    public void vote(Member member, Course course){

        CourseVoter courseVoter = new CourseVoter(member, course);
        courseVoterRepository.save(courseVoter);
    }

    @Transactional
    public void deleteVote(Course course, Member member){

        courseVoterRepository.deleteCourseVoterByCourseAndMember(course,member);
    }

    public boolean canLike(Member member, Course course) {
        if (member == null) return false;
        if (course == null) return false;
        return !course.getCourseVoters().contains(new CourseVoter(member,course));
    }
    public boolean canCancelLike(Member member, Course course) {
        if (member == null) return false;
        if (course == null) return false;

        return course.getCourseVoters().contains(new CourseVoter(member,course));
    }

}
