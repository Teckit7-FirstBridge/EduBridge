package com.ll.edubridge.domain.course.summaryNote.dto;

import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.domain.member.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SummaryNoteDto {
    private Long id;
    private LocalDateTime createDate;
    private Member member;
    private String content;
    private Long score;
    private boolean isPass;
    private Long courseId;
    private Long videoId;
    private String courseName;
    private String title;
    public SummaryNoteDto(SummaryNote summaryNote){
        this.id = summaryNote.getId();
        this.content = summaryNote.getContent();
        this.member = summaryNote.getWriter();
        this.score = summaryNote.getScore();
        this.isPass = summaryNote.getScore() >= 70;
        this.courseId = summaryNote.getVideo().getCourse().getId();
        this.videoId = summaryNote.getVideo().getId();
        this.createDate = summaryNote.getCreateDate();
        this.courseName = summaryNote.getVideo().getCourse().getTitle();
        this.title = summaryNote.getVideo().getTitle();
    }
}
