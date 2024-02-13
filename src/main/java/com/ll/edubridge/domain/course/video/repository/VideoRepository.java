package com.ll.edubridge.domain.course.video.repository;

import com.ll.edubridge.domain.course.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Video findById(Long id);

    Video findByCourseIdAndId(Long courseId, Long id);

    List<Video> findByCourseIdOrderByIdAsc(Long courseId);
}