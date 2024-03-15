package com.ll.edubridge.domain.course.video.entity;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.course.summaryNote.entity.SummaryNote;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Video extends BaseEntity {

    @Column(length = 150)
    private String url;

    @Column(length = 150)
    private String imgUrl;

    @Column(length = 300)
    private String overView;

    @ManyToOne(optional = false)
    private Course course;

    @OneToMany(mappedBy = "video", cascade = CascadeType.REMOVE)
    private List<SummaryNote> summaryNotes;

    @Column(length = 100)
    private String keywords;

    private String title;

}
