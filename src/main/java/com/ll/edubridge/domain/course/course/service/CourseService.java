package com.ll.edubridge.domain.course.course.service;

import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.dto.CreateCourseDto;
import com.ll.edubridge.domain.course.course.dto.NumDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.repository.CourseRepository;
import com.ll.edubridge.domain.course.courseEnroll.repository.CourseEnrollRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.global.exceptions.CodeMsg;
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
    private final CourseEnrollRepository courseEnrollRepository;
    private final MemberService memberService;



    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public Course create(CreateCourseDto createCourseDto) {


        int price = 2000;


        Course course = Course.builder()
                .title(createCourseDto.getTitle())
                .notice(createCourseDto.getNotice())
                .imgUrl(createCourseDto.getImgUrl())
                .overView(createCourseDto.getOverView())
                .price(price)
                .roadmapNum(0)
                .writer_id(createCourseDto.getWriter_id())
                .hashtags(createCourseDto.getHashtags())
                .writer_nickname(memberService.getMember(createCourseDto.getWriter_id()).getNickname())
                .build();

        return courseRepository.save(course);
    }

    @Transactional
    public void changeRoadmapNum(Long id, NumDto numDto){

        Course course = this.getCourse(id);

        course.setRoadmapNum(numDto.getNum());

        courseRepository.save(course);
    }

    @Transactional
    public Course modify(Long id, CourseDto courseDto) {
        Course course = this.getCourse(id);

        course.setTitle(courseDto.getTitle());
        course.setNotice(courseDto.getNotice());
        course.setImgUrl(courseDto.getImgUrl());
        course.setOverView(courseDto.getOverView());
        course.setHashtags(courseDto.getHashtags());

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
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(),CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }

    @Transactional
    public boolean haveAuthority(Long id) {
        Member member = rq.getMember();

        if (member == null) return false;

        if (rq.isAdmin() || rq.getMember().getId().equals(id)) return true;

        return false;
    }
    public Page<Course> findByKwAdmin(KwTypeCourse kwType, String kw, Member author, Pageable pageable) {
        return courseRepository.findByKwAdmin(kwType, kw, author, pageable);
    }
    public Page<Course> findByKw(KwTypeCourse kwType, String kw, Member author, Pageable pageable) {
        return courseRepository.findByKw(kwType, kw, author, pageable);
    }
    public Page<Course> findMyCourse(Member author, Pageable pageable){
        return courseRepository.findByWriterId(author,pageable);
    }

    public List<Course> findLatestCourse(int num) {
        return courseRepository.findLatestCourse(num);
    }

    @Transactional
    public void vote(Long id, Member member) {
        Course course = this.getCourse(id);
        course.getVoter().add(member);
        courseRepository.save(course);
    }

    @Transactional
    public void deleteVote(Long id, Member member){
        Course course = this.getCourse(id);
        course.getVoter().remove(member);
        courseRepository.save(course);
    }

    public boolean canLike(Member member, Course course) {
        if (member == null) return false;
        if (course == null) return false;

        return !course.getVoter().contains(member);
    }
    public boolean canCancelLike(Member member, Course course) {
        if (member == null) return false;
        if (course == null) return false;

        return course.getVoter().contains(member);
    }

    public List<Course> findByVoter(Member member){
        return courseRepository.findByVoter(member);
    }

    @Transactional
    public Course startOrstop(Course course){
        course.setConfirm(!course.getConfirm());
        return courseRepository.save(course);
    }


    public List<Course> findRecentCourse() {
        return courseRepository.findTop5ByOrderByIdDesc();
    }

}