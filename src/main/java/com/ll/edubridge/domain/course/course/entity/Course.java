package com.ll.edubridge.domain.course.course.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.edubridge.domain.CourseVoter.entity.CourseVoter;
import com.ll.edubridge.domain.course.courseEnroll.entity.CourseEnroll;
import com.ll.edubridge.domain.course.roadmap.entity.CourseRoadmap;
import com.ll.edubridge.domain.course.video.entity.Video;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Course extends BaseEntity {

    @Column(length = 200)
    private String title;

    @Column(length = 1000)
    private String notice;

    @Column(length = 150)
    private String imgUrl;

    @Column(length = 500)
    private String overView;

    private int price;

    @Column
    @Builder.Default
    private Boolean confirm = false;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Video> videoList;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CourseEnroll> courseEnrollList;

    @JsonIgnore
    @OneToMany(mappedBy = "course",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<CourseVoter> courseVoters;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<CourseRoadmap> roadmapList;

    private String hashtags;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member writer;

}
