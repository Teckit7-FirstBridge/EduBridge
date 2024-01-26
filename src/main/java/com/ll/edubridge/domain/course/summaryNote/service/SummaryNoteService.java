package com.ll.edubridge.domain.course.summaryNote.service;

import com.ll.edubridge.domain.course.summaryNote.dto.CreateSummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.dto.SummaryNoteDto;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.course.summaryNote.repository.SummaryNoteRepository;
import com.ll.edubridge.domain.course.video.dto.CreateVideoDto;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SummaryNoteService {
    private final SummaryNoteRepository summaryNoteRepository;
    private final Rq rq;

    @Transactional
    public SummaryNote create(CreateSummaryNoteDto createSummaryNoteDto) {
        SummaryNote summaryNote = SummaryNote.builder()
                .content(createSummaryNoteDto.getContent())
                .build();
        return summaryNoteRepository.save(summaryNote);
    }


}
