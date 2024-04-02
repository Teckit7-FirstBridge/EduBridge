package com.ll.edubridge.domain.course.course.service;

import com.ll.edubridge.domain.course.course.dto.CreateCourseDto;
import com.ll.edubridge.domain.course.course.dto.NumDto;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.repository.CourseRepository;
import com.ll.edubridge.domain.course.roadmap.entity.CourseRoadmap;
import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import com.ll.edubridge.domain.course.roadmap.service.RoadmapService;
import com.ll.edubridge.domain.member.member.entity.Member;
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
    private final RoadmapService roadmapService;


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
                .imgUrl("https://i.imgur.com/5JE4BGV.png")
                .overView(createCourseDto.getOverView())
                .price(price)
                .writer(rq.getMember())
                .hashtags(createCourseDto.getHashtags())
                .build();

        return courseRepository.save(course);
    }

    @Transactional
    public void changeRoadmapNum(Long roadmapId, Long courseId, NumDto numDto){

        Roadmap roadmap = roadmapService.getRoadmap(roadmapId);
        Course course = this.getCourse(courseId);

        CourseRoadmap courseRoadmap = roadmapService.getCourseRoadmap(course, roadmap);
        courseRoadmap.setCourseOrder(numDto.getNum());

        courseRepository.save(course);
    }

    @Transactional
    public Course modify(Long id, CreateCourseDto courseDto) {
        Course course = this.getCourse(id);

        course.setTitle(courseDto.getTitle());
        course.setNotice(courseDto.getNotice());
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
    public Page<Course> findMyCourse(Member writer, Pageable pageable){
        return courseRepository.findByWriter(writer, pageable);
    }



    public List<Course> findTopVotedCourses(int num){
        return courseRepository.findTopVotedCourse(num);
    }

    @Transactional
    public Course startOrStop(Course course){

        if(!course.getCourseEnrollList().isEmpty()){
            throw new GlobalException(
                    CodeMsg.E400_12_ALREADY_HAS_ENROLL.getCode(),
                    CodeMsg.E400_12_ALREADY_HAS_ENROLL.getMessage());
        }

        if(!course.getConfirm()) { // 비공개 -> 공개 상황
            String imgUrl = course.getVideoList().getFirst().getImgUrl();
            course.setImgUrl(imgUrl);
        }

        course.setConfirm(!course.getConfirm());
        return courseRepository.save(course);
    }


    public List<Course> findRecentCourse() {
        return courseRepository.findTop5ByOrderByIdDesc();
    }

}