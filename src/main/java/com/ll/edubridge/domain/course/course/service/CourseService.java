package com.ll.edubridge.domain.course.course.service;

import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.dto.CreateCourseDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.repository.CourseRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final Rq rq;
    private final CourseRepository courseRepository;

    public static List<Course> findAll() {
        return null;
    }

    public Optional<Course> findById(Long id) {
        // return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 강좌가 존재하지 않습니다."));
        return courseRepository.findById(id);
    }



    @Transactional
    public Page<Course> search(Specification<Course> spec, Pageable pageable){
        return courseRepository.findAll(spec, pageable);
    }



    @Transactional
    public Course create(CreateCourseDto createCourseDto) {
        Course course = Course.builder()
                .title(createCourseDto.getTitle())
                .notice(createCourseDto.getNotice())
                .imgUrl(createCourseDto.getImgUrl())
                .overView(createCourseDto.getOverView())
                .build();
        return CourseRepository.save(course);
    }

    @Transactional
    public Course modify(Long id, CourseDto courseDto) {
        Course course = this.getCourse(id);

        course.setImgUrl(CourseDto.getImgUrl());
        course.setOverView(CourseDto.getOverView());

        return CourseRepository.save(course);
    }

    @Transactional
    public void delete(Long id) {
        Course course = this.getCourse(id);
        CourseRepository.delete(course);
    }

    @Transactional
    public Course getCourse(Long id) {
        Optional<Course> course = this.findById(id);
        if (course.isPresent()) {
            return course.get();
        } else {
            throw new GlobalException("404-1", "해당 영상을 찾을 수 없습니다.");
        }
    }

    @Transactional
    public CourseRepository getCourseRepository() {
        return courseRepository;
    }

    @Transactional
    public boolean haveAuthority(Course course){

        Member member = rq.getMember();

        Course course = this.getCourse();

        if (member == null) return false;

        if (rq.isAdmin()) return true;

        return course.getId().equals(member);
    }
}