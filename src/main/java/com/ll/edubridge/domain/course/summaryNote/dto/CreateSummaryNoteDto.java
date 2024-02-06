package com.ll.edubridge.domain.course.summaryNote.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateSummaryNoteDto {

    @NotEmpty
    @Size(min=200)
    private String content;

    public CreateSummaryNoteDto(String content,Long video_id){
        this.content = content;
    }

    public CreateSummaryNoteDto(){

    }
}
