package com.ll.edubridge.domain.course.summaryNote.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CreateSummaryNoteDto {

    @NotEmpty
    private String content;

    public CreateSummaryNoteDto(String content,Long video_id){
        this.content = content;
    }

    public CreateSummaryNoteDto(){

    }
}
