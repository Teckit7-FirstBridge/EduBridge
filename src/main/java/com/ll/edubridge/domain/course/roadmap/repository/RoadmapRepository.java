package com.ll.edubridge.domain.course.roadmap.repository;

import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import com.ll.edubridge.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long>, CustomRoadmapRepository {

    // Page<Roadmap> findByKw(KwTypeCourse kwType, String kw, Pageable pageable);

    List<Roadmap> findByOwner(Member owner);

    // Roadmap findByCurriculumContains(Course course);

}
