package com.ll.edubridge.domain.PostVoter.entity;

import com.ll.edubridge.domain.CourseVoter.entity.CourseVoter;
import com.ll.edubridge.domain.course.course.entity.Course;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.entity.Post;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "post_member")
public class PostVoter {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public PostVoter() {
    }

    public PostVoter(Post post, Member member) {
        this.post = post;
        this.member = member;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PostVoter other = (PostVoter) obj;
        return Objects.equals(member, other.member) && Objects.equals(post, other.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, post);
    }

}
