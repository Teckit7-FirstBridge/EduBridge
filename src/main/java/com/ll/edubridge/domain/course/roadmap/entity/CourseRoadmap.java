package com.ll.edubridge.domain.course.roadmap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class CourseRoadmap extends BaseEntity {

    @ManyToOne(optional = false)
    private Course course;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Roadmap roadmap;

    private int courseOrder; // 이 course가 해당 roadmap에서 몇 번째 강좌인가
}
