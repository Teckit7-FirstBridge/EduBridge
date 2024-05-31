package com.ll.edubridge.domain.course.summaryNote.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class SummaryNote extends BaseEntity {

    @Column(columnDefinition = "Text")
    private String content;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member writer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Video video;

    @Builder.Default
    private Long score = 0L;

}
