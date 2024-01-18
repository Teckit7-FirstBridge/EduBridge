package com.ll.edubridge.domain.entity;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Cart extends BaseEntity {

    private int amount;

    @ManyToMany
    private Set<Course> courses;

    @OneToOne(optional = false)
    private Member member;

}
