package com.ll.edubridge.domain.course.summaryNote.dto;

import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class SummaryNoteDto {
    private Long id;
    private LocalDateTime createDate;
    private Long member_id;
    private String member_nickname;
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
        this.member_id = summaryNote.getWriter().getId();
        this.member_nickname = summaryNote.getWriter().getNickname();
        this.score = summaryNote.getScore();
        this.isPass = summaryNote.getScore() >= 70;
        this.courseId = summaryNote.getVideo().getCourse().getId();
        this.videoId = summaryNote.getVideo().getId();
        this.createDate = summaryNote.getCreateDate();
        this.courseName = summaryNote.getVideo().getCourse().getTitle();
        this.title = summaryNote.getVideo().getTitle();
    }
}
