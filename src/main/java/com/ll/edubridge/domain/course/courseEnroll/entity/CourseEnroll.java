package com.ll.edubridge.domain.course.courseEnroll.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
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
public class CourseEnroll extends BaseEntity {

    @JsonIgnore
    @ManyToOne(optional = false)
    private Course course;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Member member;

}
