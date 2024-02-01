package com.ll.edubridge.domain.home.admin.dto;

import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class RecentSummaryNoteDto {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String courseName;



    public RecentSummaryNoteDto(SummaryNote summaryNote) {
        this.id = summaryNote.getId();
        this.name = summaryNote.getWriter().getNickname();
        this.courseName=summaryNote.getVideo().getCourse().getTitle();
    }
}
