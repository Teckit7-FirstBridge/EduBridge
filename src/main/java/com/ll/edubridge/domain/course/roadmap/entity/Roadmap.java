package com.ll.edubridge.domain.course.roadmap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.edubridge.domain.member.member.entity.Member;
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
public class Roadmap extends BaseEntity {

    @Column(length = 50)
    private String title;

    @Column(length = 300)
    private String overView;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.REMOVE)
    private List<CourseRoadmap> curriculum;

    @Column
    private String hashtags;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member owner;
}
