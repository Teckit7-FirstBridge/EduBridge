package com.ll.edubridge.domain.course.video.repository;

import com.ll.edubridge.domain.course.video.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Video findById(String url);
    Page<Video> findByCourseId(Pageable pageable, int courseId); // SQL 문 필요할 수 있음
}