package com.ll.edubridge.domain.course.roadmap.repository;

import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import com.ll.edubridge.domain.member.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long>, CustomRoadmapRepository {

    Page<Roadmap> findByOwnerOrderByCreateDateDesc(Member owner, Pageable pageable);

    List<Roadmap> findByOwnerOrderByCreateDateDesc(Member owner);

}
