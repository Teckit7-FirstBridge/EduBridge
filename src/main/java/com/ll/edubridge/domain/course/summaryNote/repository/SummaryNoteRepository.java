package com.ll.edubridge.domain.course.summaryNote.repository;


import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SummaryNoteRepository extends JpaRepository<SummaryNote, Long> {
    Optional<SummaryNote> findById(Long id);

    Page<SummaryNote> findByVideoId(Pageable pageable, Long videoId);

    List<SummaryNote> findTop5ByOrderByIdDesc();


    List<SummaryNote> findByWriterIdAndScoreGreaterThanEqual(Long id, Long score);

    @Query("from SummaryNote s where s.video.course.id = :courseId and s.writer.id = :memberId order by s.video.id asc")
    List<SummaryNote> findByWriterIdAndCourseId(Long memberId, Long courseId);

    @Modifying
    @Transactional
    @Query("DELETE FROM SummaryNote s WHERE s.writer.id = :writerId AND s.video.course.id = :courseId")
    void deleteByWriterIdAndCourseId(@Param("writerId") Long writerId, @Param("courseId") Long courseId);
}
