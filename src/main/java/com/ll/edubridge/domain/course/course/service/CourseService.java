package com.ll.edubridge.domain.course.course.service;

import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.dto.CreateCourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.repository.CourseRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.standard.base.KwTypeCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;
    private final Rq rq;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }



    @Transactional
    public Course create(CreateCourseDto createCourseDto) {
        Course course = Course.builder()
                .title(createCourseDto.getTitle())
                .notice(createCourseDto.getNotice())
                .imgUrl(createCourseDto.getImgUrl())
                .overView(createCourseDto.getOverView())
                .price(createCourseDto.getPrice())
                .build();
        return courseRepository.save(course);
    }

    @Transactional
    public Course modify(Long id, CourseDto courseDto) {
        Course course = this.getCourse(id);

        course.setTitle(courseDto.getTitle());
        course.setNotice(courseDto.getNotice());
        course.setImgUrl(courseDto.getImgUrl());
        course.setOverView(courseDto.getOverView());

        return courseRepository.save(course);
    }

    @Transactional
    public void delete(Long id) {
        Course course = this.getCourse(id);
        courseRepository.delete(course);
    }

    @Transactional
    public Course getCourse(Long id) {
        Optional<Course> course = this.findById(id);
        if (course.isPresent()) {
            return course.get();
        } else {
            throw new GlobalException("404-1", "해당 강좌을 찾을 수 없습니다.");
        }
    }

    @Transactional
    public boolean haveAuthority(Long id) {
        Member member = rq.getMember();

        if (member == null) return false;

        if (!rq.isAdmin()) return true;

        return true;
    }

    public Page<Course> findByKw(KwTypeCourse kwType, String kw, Member author, Pageable pageable) {
        return courseRepository.findByKw(kwType, kw, author, pageable);
    }

    public List<Course> findLatestCourse(int num) {
        return courseRepository.findLatestCourse(num);
    }

}