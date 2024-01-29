package com.ll.edubridge.domain.course.summaryNote.service;

import com.ll.edubridge.domain.course.summaryNote.dto.CreateSummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.dto.SummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.repository.SummaryNoteRepository;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SummaryNoteService {
    private final SummaryNoteRepository summaryNoteRepository;
    private final Rq rq;

    public Page<SummaryNote> findAll(Pageable pageable) {
        return summaryNoteRepository.findAll(pageable);
    }

    public Optional<SummaryNote> findById(Long id) {
        return summaryNoteRepository.findById(id);
    }


    @Transactional
    public SummaryNote getSummaryNote(Long id) {
        Optional<SummaryNote> summaryNote = this.findById(id);
        if (summaryNote.isPresent()) {
            return summaryNote.get();
        } else {
            throw new GlobalException("404-1", "해당 강의 요약노트를 찾을 수 없습니다.");
        }
    }



    @Transactional
    public SummaryNote create(Member member, CreateSummaryNoteDto createSummaryNoteDto) {
        SummaryNote summaryNote = SummaryNote.builder()
                .content(createSummaryNoteDto.getContent())
                .build();
        return summaryNoteRepository.save(summaryNote);
    }

    @Transactional
    public SummaryNote modify(Long id , SummaryNoteDto summaryNoteDto) {
        SummaryNote summaryNote = this.getSummaryNote(id);

        summaryNote.setContent(summaryNoteDto.getContent());

        return summaryNoteRepository.save(summaryNote);
    }

    @Transactional
    public void delete(Long id) {
        SummaryNote summaryNote = this.getSummaryNote(id);
        summaryNoteRepository.delete(summaryNote);
    }

    @Transactional
    public boolean haveAuthority(Long id) {
        Member member = rq.getMember();

        if (member == null) return false;

        if (rq.isAdmin()) return true;

        return true;
    }



}
