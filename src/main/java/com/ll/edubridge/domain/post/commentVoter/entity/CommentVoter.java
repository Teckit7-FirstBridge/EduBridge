package com.ll.edubridge.domain.post.commentVoter.entity;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Entity
@Getter
@Table(name = "comment_member")
public class CommentVoter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public CommentVoter(Member member, Comment comment){
        this.comment = comment;
        this.member = member;
    }

    public CommentVoter() {}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CommentVoter other = (CommentVoter) obj;
        return Objects.equals(member, other.member) && Objects.equals(comment, other.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, comment);
    }
}
