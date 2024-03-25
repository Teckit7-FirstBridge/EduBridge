package com.ll.edubridge.domain.course.roadmap.service;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.repository.CourseRepository;
import com.ll.edubridge.domain.course.roadmap.dto.CreateRoadmapDto;
import com.ll.edubridge.domain.course.roadmap.entity.CourseRoadmap;
import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import com.ll.edubridge.domain.course.roadmap.repository.CourseRoadmapRepository;
import com.ll.edubridge.domain.course.roadmap.repository.RoadmapRepository;
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
public class RoadmapService {
    private final RoadmapRepository roadmapRepository;
    private final Rq rq;
    private final CourseRepository courseRepository;
    private final CourseRoadmapRepository courseRoadmapRepository;

    public List<Roadmap> findAll() {
        return roadmapRepository.findAll();
    }
    public Optional<Roadmap> findById(Long id) {
        return roadmapRepository.findById(id);
    }

    public Page<Roadmap> findByKw(KwTypeCourse kwType, String kw, Pageable pageable) {
        return roadmapRepository.findByKw(kwType, kw, pageable);
    }

    public Roadmap getRoadmap(Long id) {
        Optional<Roadmap> roadmap = this.findById(id);
        if(roadmap.isPresent()) {
            return roadmap.get();
        } else {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(),CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }

    // 강좌가 속한 로드맵 목록 찾기
    public List<Roadmap> getCourseRoadmapList(Course course) {
        return courseRoadmapRepository.findByCourse(course)
                .stream()
                .map(CourseRoadmap::getRoadmap)
                .toList();
    }

    // CourseRoadmap 개체 찾기
    public CourseRoadmap getCourseRoadmap(Course course, Roadmap roadmap) {
        return courseRoadmapRepository.findByCourseAndRoadmap(course, roadmap);
    }

    public CourseRoadmap getCourseRoadmapById(Long id) {
        return courseRoadmapRepository.findCourseRoadmapById(id);
    }

    public List<Roadmap> getMyRoadmaps(Member member) {
        return roadmapRepository.findByOwnerOrderByCreateDateDesc(member);
    }

    @Transactional
    public Roadmap create(CreateRoadmapDto createroadmapDto) {

        Roadmap roadmap = Roadmap.builder()
                .title(createroadmapDto.getTitle())
                .overView(createroadmapDto.getOverView())
                .hashtags(createroadmapDto.getHashtags())
                .owner(rq.getMember())
                .build();

        return roadmapRepository.save(roadmap);
    }

    @Transactional
    public void addCourse(Long id, Course course, int courseOrder) {

        Roadmap roadmap = this.getRoadmap(id);

        if(courseRoadmapRepository.existsByCourseAndRoadmap(course, roadmap)){
            this.changeCourseOrder(course, roadmap, courseOrder);
        }else {
            CourseRoadmap courseRoadmap = new CourseRoadmap(course, roadmap, courseOrder);
            courseRoadmapRepository.save(courseRoadmap);
        }
    }

    @Transactional
    public void changeCourseOrder(Course course, Roadmap roadmap, int courseOrder) {

        CourseRoadmap courseRoadmap = courseRoadmapRepository.findByCourseAndRoadmap(course, roadmap);

        courseRoadmap.setCourseOrder(courseOrder);

        courseRoadmapRepository.save(courseRoadmap);
    }

    @Transactional
    public Roadmap modify(Long id, CreateRoadmapDto roadmapDto) {
        Roadmap roadmap = this.getRoadmap(id);

        roadmap.setTitle(roadmapDto.getTitle());
        roadmap.setOverView(roadmapDto.getOverView());
        roadmap.setHashtags(roadmapDto.getHashtags());

        return roadmapRepository.save(roadmap);
    }

    @Transactional
    public void delete(Long id) {
        Roadmap roadmap = this.getRoadmap(id);
        roadmapRepository.delete(roadmap);
    }

    @Transactional
    public void courseRoadmapDelete(Long id) {
        CourseRoadmap courseRoadmap = this.getCourseRoadmapById(id);
        courseRoadmapRepository.delete(courseRoadmap);
    }

    @Transactional
    public void courseRoadmapDelete(Roadmap roadmap, Course course) {
        CourseRoadmap courseRoadmap = this.getCourseRoadmap(course, roadmap);
        courseRoadmapRepository.delete(courseRoadmap);
    }
}
