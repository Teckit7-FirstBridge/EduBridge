package com.ll.edubridge.domain.CourseVoter.service;

import com.ll.edubridge.domain.CourseVoter.entity.CourseVoter;
import com.ll.edubridge.domain.CourseVoter.repositry.CourseVoterRepository;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.repository.CourseRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseVoterService {
    private final CourseVoterRepository courseVoterRepository;
    private final CourseRepository courseRepository;

    public Boolean isVote(Member member,Course course){
        return false;
    }
    public void vote(Member member, Course course){

        CourseVoter courseVoter = new CourseVoter(member, course);
        courseVoterRepository.save(courseVoter);
//        course.getCourseVoters().stream().forEach(courseVoter1 -> System.out.println(courseVoter1.getMember() + " voted for " + courseVoter1.getCourse()));
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

    public List<CourseVoter> findByVoter(Member member){
        return courseVoterRepository.findByMember(member);
    }

}
