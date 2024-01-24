package com.ll.edubridge.domain.course.video.repository;

import com.ll.edubridge.domain.course.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Video findById(Long id);

    List<Video> findByCourseId(Long courseId);

//    @Query("select "
//            + "v.id, v.overView "
//            + "from Video v "
//            + "where (v.course.id = :courseId)")
//    List<Video> findByCourseId(@Param("courseId") Long courseId);

}