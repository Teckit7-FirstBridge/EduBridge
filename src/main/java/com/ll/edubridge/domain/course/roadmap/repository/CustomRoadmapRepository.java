package com.ll.edubridge.domain.course.roadmap.repository;

import com.ll.edubridge.domain.course.roadmap.entity.Roadmap;
import com.ll.edubridge.standard.base.KwTypeCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomRoadmapRepository {
    Page<Roadmap> findByKw(KwTypeCourse kwType, String kw, Pageable pageable);
}
