package com.ll.edubridge.domain.post.comment.entity;

import com.ll.edubridge.domain.CommentVoter.entity.CommentVoter;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Comment extends BaseEntity {

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Post post;

    @OneToMany(mappedBy = "comment",cascade = CascadeType.REMOVE)
    Set<CommentVoter> commentVoters;

    @ManyToOne(optional = false)
    private Member writer;

    @Formula("(select count(*) from comment_member cm where cm.comment_id = id)")
    private int voteCount;
}
