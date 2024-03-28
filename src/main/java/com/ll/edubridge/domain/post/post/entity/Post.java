package com.ll.edubridge.domain.post.post.entity;


import com.ll.edubridge.domain.PostVoter.entity.PostVoter;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.post.comment.entity.Comment;
import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Post extends BaseEntity {

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "Text")
    private String content;

    //  1대1 문의는 자동 으로 비밀글 설정
    private boolean published;

    private boolean report;

    @ManyToOne(optional = false)
    private Member writer;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE)
    Set<PostVoter> postVoters;

    @Formula("(select count(*) from post_member pm where pm.post_id = id)")
    private int voteCount;
}
