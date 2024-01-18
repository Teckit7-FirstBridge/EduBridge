package com.ll.edubridge.domain.post.comment.entity;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.post.entity.Post;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
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
public class Comment extends BaseEntity {

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Post post;

    @ManyToMany
    Set<Member> voter;

    @ManyToOne(optional = false)
    private Member writer;

}
