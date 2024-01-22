package com.ll.edubridge.domain.course.course.entity.service;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ll.edubridge.domain.course.course.entity.QCourse.course;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {
    private Rq rq;

    public static List<Course> findAll() {
    }

    public boolean haveAuthority(Comment comment){

        Member member=rq.getMember();

        if(member==null) return false;

        return comment.getWriter().equals(course);
    }

    public Page<Course> search(){

    }
}
