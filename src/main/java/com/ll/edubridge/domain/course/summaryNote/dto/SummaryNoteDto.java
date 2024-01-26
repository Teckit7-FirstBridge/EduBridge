package com.ll.edubridge.domain.course.summaryNote.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import lombok.Getter;

@Getter
public class SummaryNoteDto {
    private Long id;
    private String content;

    public SummaryNoteDto(SummaryNote summaryNote){
        this.id = summaryNote.getId();
        this.content = summaryNote.getContent();
    }
}
