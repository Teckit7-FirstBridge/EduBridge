package com.ll.edubridge.domain.course.video.repository;

import com.ll.edubridge.domain.course.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Video findById(Long id);

    List<Video> findByCourseId(Long courseId); // SQL 문 필요할 수 있음

    /*
    @Query("select "
            + "distinct v "
            + "from Video v "
            + "left outer join Course c1 on c1=v.course "
            + "where (c1 = :courseId) ")
    List<Video> findByCourseId(@Param("courseId") Long courseId);
     */
}