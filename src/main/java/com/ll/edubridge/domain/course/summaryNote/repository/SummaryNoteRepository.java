package com.ll.edubridge.domain.course.summaryNote.repository;


import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryNoteRepository extends JpaRepository<SummaryNote, Long> {

}
