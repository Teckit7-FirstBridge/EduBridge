package com.ll.edubridge.domain.course.summaryNote.repository;


import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SummaryNoteRepository extends JpaRepository<SummaryNote, Long> {
    Optional<SummaryNote> findById(Long id);
    Page<SummaryNote> findByVideoId(Pageable pageable,Long videoid);

    List<SummaryNote> findTop5ByOrderByIdDesc();
}
