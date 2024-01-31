package com.ll.edubridge.domain.course.summaryNote.dto;

import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import lombok.Getter;

@Getter
public class CreateSummaryNoteDto {

    private String content;

    public CreateSummaryNoteDto(String content,Long video_id){
        this.content = content;
    }

    public CreateSummaryNoteDto(){

    }
}
