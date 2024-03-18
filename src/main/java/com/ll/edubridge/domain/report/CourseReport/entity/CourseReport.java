package com.ll.edubridge.domain.report.CourseReport.entity;


import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
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
public class CourseReport extends BaseEntity {

    @Column(length = 500)
    private String content;

    @ManyToOne(optional = false)
    private Member writer;

    @Column()
    private Long courseId;

}
