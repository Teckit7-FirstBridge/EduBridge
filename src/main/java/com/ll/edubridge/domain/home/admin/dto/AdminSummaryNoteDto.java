package com.ll.edubridge.domain.home.admin.dto;

import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
public class AdminSummaryNoteDto {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String courseName;
    @NonNull
    private Long videoId;
    @NonNull
    private Long courseId;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private boolean isPass;


    public AdminSummaryNoteDto(SummaryNote summaryNote) {
        this.id = summaryNote.getId();
        this.name = summaryNote.getWriter().getNickname();
        this.courseName = summaryNote.getVideo().getCourse().getTitle();
        this.videoId = summaryNote.getVideo().getId();
        this.courseId = summaryNote.getVideo().getCourse().getId();
        this.createDate = summaryNote.getCreateDate();
        this.isPass = summaryNote.getScore() >= 70;
    }
}
