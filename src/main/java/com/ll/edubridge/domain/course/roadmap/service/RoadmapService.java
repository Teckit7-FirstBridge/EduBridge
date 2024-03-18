package com.ll.edubridge.domain.course.roadmap.service;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.course.repository.CourseRepository;
import com.ll.edubridge.domain.course.roadmap.dto.CreateRoadmapDto;
import com.ll.edubridge.domain.course.roadmap.dto.RoadmapDto;
import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
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

    public Roadmap getCourseRoadmap(Course course) {
        return roadmapRepository.findByCurriculumContains(course);
    }

    public List<Roadmap> getMyRoadmaps(Member member) {
        return roadmapRepository.findByOwner(member.getUsername());
    }

    @Transactional
    public Roadmap create(CreateRoadmapDto createroadmapDto) {

        Roadmap roadmap = Roadmap.builder()
                .title(createroadmapDto.getTitle())
                .overView(createroadmapDto.getOverView())
                .hashtags(createroadmapDto.getHashtags())
                .owner(rq.getMember().getUsername())
                .build();

        return roadmapRepository.save(roadmap);
    }

    @Transactional
    public void addCourse(Long id, Course course) {
        Roadmap roadmap = this.getRoadmap(id);

        course.setRoadmap(roadmap);

        courseRepository.save(course);
    }

    public Roadmap modify(Long id, RoadmapDto roadmapDto) {
        Roadmap roadmap = this.getRoadmap(id);

        roadmap.setTitle(roadmapDto.getTitle());
        roadmap.setOverView(roadmap.getOverView());
        roadmap.setCurriculum(roadmap.getCurriculum());
        roadmap.setHashtags(roadmap.getHashtags());

        return roadmapRepository.save(roadmap);
    }

    public void delete(Long id) {
        Roadmap roadmap = this.getRoadmap(id);
        roadmapRepository.delete(roadmap);
    }
}
