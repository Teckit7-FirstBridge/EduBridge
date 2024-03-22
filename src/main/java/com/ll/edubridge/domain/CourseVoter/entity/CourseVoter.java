package com.ll.edubridge.domain.CourseVoter.entity;

import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Entity
@Getter
@Table(name = "course_member")
public class CourseVoter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public CourseVoter() {
    }

    public CourseVoter(Member member, Course course){
        this.course = course;
        this.member = member;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CourseVoter other = (CourseVoter) obj;
        return Objects.equals(member, other.member) && Objects.equals(course, other.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, course);
    }

}
