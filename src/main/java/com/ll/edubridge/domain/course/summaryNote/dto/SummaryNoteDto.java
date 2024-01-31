package com.ll.edubridge.domain.course.summaryNote.dto;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;

@Getter
public class SummaryNoteDto {
    private Long id;
    private Member member;
    private String content;

    public SummaryNoteDto(SummaryNote summaryNote){
        this.id = summaryNote.getId();
        this.content = summaryNote.getContent();
        this.member = summaryNote.getWriter();
    }
}
